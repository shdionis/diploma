package ru.yandex.sharov.example.notes.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ru.yandex.sharov.example.notes.util.UIUtil;

public class DBHelperStub {

    private static final Object lock = new Object();
    private static DBHelperStub instance;

    @NonNull
    private Comparator<Note> comparator;
    @NonNull
    private String filterQuery;
    @NonNull
    private List<Note> data;

    @NonNull
    public static DBHelperStub getInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new DBHelperStub();
                }
                return instance;
            }
        } else return instance;
    }

    private DBHelperStub() {
        comparator = UIUtil.ASC_NOTE_COMPARATOR;
        filterQuery = "";
        initData();
    }

    private void initData() {
        data = new ArrayList<>();
        for (int i = 1; i <= 50; i++) {
            StringBuilder textNote = new StringBuilder();
            for (int j = 0; j < 30; j++) {
                textNote.append("Note").append(i).append(j);
            }
            Note note = new Note("Note" + i, System.currentTimeMillis() - i * 30 * 1000 * 60, textNote.toString());
            data.add(note);
        }
        Collections.sort(data, comparator);
    }

    @NonNull
    public List<Note> getData() {
        List<Note> resultData = filterData();
        Collections.sort(resultData, comparator);
        return resultData;
    }

    @NonNull
    private List<Note> filterData() {
        List<Note> filteredData = new ArrayList<>();
        for(Note note : data) {
            if(note.getTitle().toLowerCase().contains(filterQuery) || note.getText().toLowerCase().contains(filterQuery)) {
                filteredData.add(note);
            }
        }
        return filteredData;
    }

    public void addOrUpdateNote(@NonNull Note note) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getId() == note.getId()) {
                data.remove(i);
                data.add(i, note);
                return;
            }
        }
        data.add(note);
    }

    public void removeNote(int noteId) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getId() == noteId) {
                data.remove(i);
                return;
            }
        }
    }

    @Nullable
    public Note getNoteById(int noteId) {
        for (Note note : data) {
            if (note.getId() == noteId) {
                return note;
            }
        }
        return null;
    }

    public void resortData(boolean isAscOrder) {
        comparator = isAscOrder ? UIUtil.ASC_NOTE_COMPARATOR : UIUtil.DESC_NOTE_COMPARATOR;
    }

    public void setFilterData(@NonNull String query) {
        filterQuery = query.toLowerCase();
    }


}
