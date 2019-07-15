package ru.yandex.sharov.example.notes.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ru.yandex.sharov.example.notes.interact.LocalRepositoryNoteInteractor;
import ru.yandex.sharov.example.notes.interact.NetworkInteractionsFactory;
import ru.yandex.sharov.example.notes.model.Note;
import ru.yandex.sharov.example.notes.util.UIUtil;

public class NoteListViewModel extends ViewModel implements NoteListDataProvider {
    @NonNull
    private final MutableLiveData<List<Note>> data = new MutableLiveData<>();
    @NonNull
    private final MutableLiveData<Boolean> showProgressBar = new MutableLiveData<>();
    @NonNull
    private final MutableLiveData<Integer> errorData = new MutableLiveData<>();
    @NonNull
    private LiveData<List<Note>> fullData;
    @NonNull
    private Observer<List<Note>> fullDataObserver;
    @NonNull
    private Comparator<Note> comparator;
    @NonNull
    private String filterQuery;
    @NonNull
    private LocalRepositoryNoteInteractor dbInteractor;

    public NoteListViewModel(@NonNull LocalRepositoryNoteInteractor dbInteractor) {
        comparator = UIUtil.ASC_NOTE_COMPARATOR;
        filterQuery = "";
        showProgressBar.setValue(Boolean.TRUE);
        fullDataObserver = new FullDataObserver(dbInteractor, errorData);
        this.dbInteractor = dbInteractor;
        fullData = dbInteractor.getData();
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
    public MutableLiveData<Integer> getErrorData() {
        return errorData;
    }

    @NonNull
    public LiveData<Boolean> isShowProgressBar() {
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
    }

    public void syncData() {
        //TODO: Синхронизация
    }

    public void pullData() {
        showProgressBar.setValue(Boolean.TRUE);
        NetworkInteractionsFactory.createGetRemoteNotesTask(dbInteractor, errorData).execute();
    }

    public void hideProgressBar() {
        showProgressBar.setValue(Boolean.FALSE);
    }

    private class FullDataObserver implements Observer<List<Note>> {

        private LocalRepositoryNoteInteractor dbInteractor;
        private MutableLiveData<Integer> errorData;

        public FullDataObserver(LocalRepositoryNoteInteractor dbInteractor, MutableLiveData<Integer> errorData) {
            this.dbInteractor = dbInteractor;
            this.errorData = errorData;
        }

        @Override
        public void onChanged(@NonNull List<Note> notes) {
            if(notes.isEmpty()) {
                NetworkInteractionsFactory.createGetRemoteNotesTask(dbInteractor, errorData).execute();
                return;
            }
            if (showProgressBar.getValue() == null || showProgressBar.getValue() == Boolean.TRUE) {
                showProgressBar.setValue(Boolean.FALSE);
            }
            refreshData(notes);
        }
    }
}
