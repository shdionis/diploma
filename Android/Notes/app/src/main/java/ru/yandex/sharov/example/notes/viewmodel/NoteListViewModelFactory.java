package ru.yandex.sharov.example.notes.viewmodel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ru.yandex.sharov.example.notes.data.NoteInteractor;

public class NoteListViewModelFactory implements ViewModelProvider.Factory {

    @NonNull
    private final NoteInteractor dbInteractor;

    public NoteListViewModelFactory(@NonNull Context context) {
        dbInteractor = NoteInteractor.getInstance(context);
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(NoteListViewModel.class)) {
            return (T) new NoteListViewModel(dbInteractor);
        } else if (modelClass.isAssignableFrom(NoteViewModel.class)) {
            return (T) new NoteViewModel(dbInteractor);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class!");
        }
    }
}
