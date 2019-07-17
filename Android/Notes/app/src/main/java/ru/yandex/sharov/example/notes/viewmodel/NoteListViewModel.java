package ru.yandex.sharov.example.notes.viewmodel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ru.yandex.sharov.example.notes.R;
import ru.yandex.sharov.example.notes.entities.Note;
import ru.yandex.sharov.example.notes.interact.NotesUseCases;
import ru.yandex.sharov.example.notes.interact.interactions.Type;
import ru.yandex.sharov.example.notes.util.UIUtil;

public class NoteListViewModel extends ViewModel implements NoteListDataProvider {
    @NonNull
    private final MutableLiveData<List<Note>> data = new MutableLiveData<>();
    @NonNull
    private final MutableLiveData<Boolean> showProgressBar = new MutableLiveData<>();
    @NonNull
    private final MutableLiveData<State> stateInteraction = new MutableLiveData<>();
    @NonNull
    private LiveData<List<Note>> fullData;
    @NonNull
    private Observer<List<Note>> fullDataObserver;
    @NonNull
    private Comparator<Note> comparator;
    @NonNull
    private String filterQuery;
    @NonNull
    private NotesUseCases interactor;

    public NoteListViewModel(@NonNull NotesUseCases interactor) {
        this.interactor = interactor;
        comparator = UIUtil.ASC_NOTE_COMPARATOR;
        filterQuery = "";
        fullDataObserver = new FullDataObserver();
        fullData = interactor.getAllNotes();
        showProgressBar.setValue(Boolean.TRUE);
        fullData.observeForever(fullDataObserver);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        fullData.removeObserver(fullDataObserver);
    }

    @NonNull
    public LiveData<List<Note>> getData() {
        return data;
    }

    @NonNull
    public LiveData<State> getStateInteraction() {
        return stateInteraction;
    }

    @NonNull
    public LiveData<Boolean> getShowProgressBarStatus() {
        return showProgressBar;
    }

    @NonNull
    private List<Note> filterData(@NonNull List<Note> notes) {
        List<Note> filteredData = new ArrayList<>();
        for (Note note : notes) {
            if (note.getTitle().toLowerCase().contains(filterQuery) ||
                    (note.getContent() != null && note.getContent().toLowerCase().contains(filterQuery))) {
                filteredData.add(note);
            }
        }
        return filteredData;
    }

    public void resortData(boolean isAscOrder) {
        comparator = isAscOrder ? UIUtil.ASC_NOTE_COMPARATOR : UIUtil.DESC_NOTE_COMPARATOR;
    }

    public void setFilterData(@NonNull String query) {
        filterQuery = query.toLowerCase();
    }

    private void refreshData(@NonNull List<Note> notes) {
        List<Note> resultData = filterData(notes);
        Collections.sort(resultData, comparator);
        data.setValue(resultData);
    }

    public void refreshData() {
        if (fullData.getValue() != null) {
            refreshData(fullData.getValue());
        } else {
            refreshData(Collections.emptyList());
        }
        stateInteraction.setValue(new State(Type.SUCCESS));
    }

    public void syncData() {
        //TODO: Синхронизация
    }

    public void pullData() {
        showProgressBar();
        interactor.pullDataToLocalStorage(stateRestInteraction -> {
            if (stateRestInteraction == null) {
                hideProgressBar();
                return;
            }
            int message;
            switch (stateRestInteraction.getCode()) {
                case HttpURLConnection.HTTP_NOT_FOUND:
                    message = R.string.http_not_found_error;
                    break;
                case HttpURLConnection.HTTP_INTERNAL_ERROR:
                    message = R.string.http_internal_error;
                    break;
                case HttpURLConnection.HTTP_UNAUTHORIZED:
                    message = R.string.http_unauthorized_error;
                    break;
                case HttpURLConnection.HTTP_FORBIDDEN:
                    message = R.string.http_forbidden_error;
                    break;
                default:
                    message = R.string.http_connection_error;
            }
            stateInteraction.setValue(new State(stateRestInteraction.getType(), message));
            refreshData();
            hideProgressBar();
        });
    }

    public void hideProgressBar() {
        if (Boolean.TRUE.equals(showProgressBar.getValue())) {
            showProgressBar.setValue(Boolean.FALSE);
        }
    }

    public void showProgressBar() {
        if (Boolean.FALSE.equals(showProgressBar.getValue())) {
            showProgressBar.setValue(Boolean.TRUE);
        }
    }

    private class FullDataObserver implements Observer<List<Note>> {
        @Override
        public void onChanged(@NonNull List<Note> notes) {
            if (notes.isEmpty()) {
                pullData();
                return;
            }
            hideProgressBar();
            refreshData(notes);
        }
    }

    public static class State {
        @NonNull
        private Type type;
        @Nullable
        private int errorMessage;

        public State(@NonNull Type type, int errorMessage) {
            this.type = type;
            this.errorMessage = errorMessage;
        }

        public State(@NonNull Type type) {
            this.type = type;
        }

        @NonNull
        public Type getType() {
            return type;
        }

        @Nullable
        public int getErrorMessage() {
            return errorMessage;
        }


    }
}
