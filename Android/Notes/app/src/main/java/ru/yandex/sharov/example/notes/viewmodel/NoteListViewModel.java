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

import ru.yandex.sharov.example.notes.data.NoteInteractor;
import ru.yandex.sharov.example.notes.data.Note;
import ru.yandex.sharov.example.notes.util.UIUtil;

public class NoteListViewModel extends ViewModel implements NoteListDataProvider {
    @NonNull
    private final MutableLiveData<List<Note>> data = new MutableLiveData<>();
    @NonNull
    private final MutableLiveData<Boolean> showProgressBar = new MutableLiveData<>();
    @NonNull
    private LiveData<List<Note>> fullData;
    @NonNull
    private Observer<List<Note>> fullDataObserver;
    @NonNull
    private Comparator<Note> comparator;
    @NonNull
    private String filterQuery;

    public NoteListViewModel(@NonNull NoteInteractor dbInteractor) {
        comparator = UIUtil.ASC_NOTE_COMPARATOR;
        filterQuery = "";
        fullDataObserver = new FullDataObserver();
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

    private class FullDataObserver implements Observer<List<Note>> {
        @Override
        public void onChanged(@NonNull List<Note> notes) {
            if (showProgressBar.getValue() == null || showProgressBar.getValue() == Boolean.FALSE) {
                showProgressBar.setValue(Boolean.TRUE);
            }
            refreshData(notes);
        }
    }
}
