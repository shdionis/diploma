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

import ru.yandex.sharov.example.notes.data.DBHelperStub;
import ru.yandex.sharov.example.notes.data.Note;
import ru.yandex.sharov.example.notes.util.UIUtil;

public class NoteListViewModel extends ViewModel {
    private MutableLiveData<List<Note>> data = new MutableLiveData<>();
    @NonNull
    private LiveData<List<Note>> fullData;
    @NonNull
    private Observer<List<Note>> fullDataObserver;
    @NonNull
    private Comparator<Note> comparator;
    @NonNull
    private String filterQuery;

    public NoteListViewModel(@NonNull DBHelperStub dbHelper) {
        comparator = UIUtil.ASC_NOTE_COMPARATOR;
        filterQuery = "";
        fullDataObserver = new FullDataObserver<List<Note>>();
        fullData = dbHelper.getData();
        fullData.observeForever(fullDataObserver);
    }

    @Override
    protected void onCleared() {
        fullData.removeObserver(fullDataObserver);
    }

    @NonNull
    public LiveData<List<Note>> getData() {
        return data;
    }

    @NonNull
    private List<Note> filterData(@NonNull List<Note> notes) {
        List<Note> filteredData = new ArrayList<>();
        for(Note note : notes) {
            if(note.getTitle().toLowerCase().contains(filterQuery) || note.getText().toLowerCase().contains(filterQuery)) {
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

    public void refreshData(){
        refreshData(fullData.getValue());
    }

    private class FullDataObserver<T> implements Observer<List<Note>> {
        @Override
        public void onChanged(@NonNull List<Note> notes) {
            refreshData(notes);
        }
    }
}
