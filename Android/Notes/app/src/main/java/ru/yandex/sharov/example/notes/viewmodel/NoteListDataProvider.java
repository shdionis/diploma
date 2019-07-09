package ru.yandex.sharov.example.notes.viewmodel;

import androidx.annotation.NonNull;

public interface NoteListDataProvider {
    void setFilterData(@NonNull String query);

    void resortData(boolean isAscOrder);

    void refreshData();
}
