package ru.yandex.sharov.example.notes.viewmodel.factory;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ru.yandex.sharov.example.notes.data.NoteInteractor;
import ru.yandex.sharov.example.notes.viewmodel.NoteViewModel;

public class NoteViewModelFactory implements ViewModelProvider.Factory {

    private static final String LOG_TAG = "[LOG_TAG:NSVMFactory]";
    @NonNull
    private final NoteInteractor dbInteractor;

    public NoteViewModelFactory(@NonNull Context context) {
        dbInteractor = NoteInteractor.getInstance(context);
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        Log.d(LOG_TAG, "create");
        if (modelClass.isAssignableFrom(NoteViewModel.class)) {
            return (T) new NoteViewModel(dbInteractor);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class!");
        }
    }
}
