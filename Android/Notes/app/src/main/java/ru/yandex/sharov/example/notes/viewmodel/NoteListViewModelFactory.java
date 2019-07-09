package ru.yandex.sharov.example.notes.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ru.yandex.sharov.example.notes.data.DBHelperStub;

public class NoteListViewModelFactory implements ViewModelProvider.Factory {

    @NonNull
    private final DBHelperStub DB_HELPER = DBHelperStub.getInstance();

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(NoteListViewModel.class)) {
            return (T) new NoteListViewModel(DB_HELPER);
        } else if(modelClass.isAssignableFrom(NoteViewModel.class)) {
            return (T) new NoteViewModel(DB_HELPER);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class!");
        }
    }
}
