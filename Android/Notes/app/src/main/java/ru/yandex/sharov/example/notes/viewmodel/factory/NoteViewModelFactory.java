package ru.yandex.sharov.example.notes.viewmodel.factory;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ru.yandex.sharov.example.notes.interact.NoteUseCases;
import ru.yandex.sharov.example.notes.interact.NotesInteractor;
import ru.yandex.sharov.example.notes.repository.local.LocalStorageRepository;
import ru.yandex.sharov.example.notes.repository.local.PreferencesRepository;
import ru.yandex.sharov.example.notes.repository.remote.RemoteServiceRepository;
import ru.yandex.sharov.example.notes.viewmodel.NoteViewModel;

public class NoteViewModelFactory implements ViewModelProvider.Factory {

    private static final String LOG_TAG = "[LOG_TAG:NSVMFactory]";
    @NonNull
    private final LocalStorageRepository localStorageRepository;
    @NonNull
    private final PreferencesRepository preferencesRepository;
    @NonNull
    private final RemoteServiceRepository remoteServiceRepository;
    @NonNull
    private final NoteUseCases interactor;

    public NoteViewModelFactory(@NonNull Context context) {
        this.localStorageRepository = LocalStorageRepository.getInstance(context);
        this.preferencesRepository = PreferencesRepository.getInstance(context);
        this.remoteServiceRepository = RemoteServiceRepository.getInstance();
        this.interactor = NotesInteractor.getInstance(
                localStorageRepository,
                remoteServiceRepository,
                preferencesRepository
        );
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        Log.d(LOG_TAG, "create");
        if (modelClass.isAssignableFrom(NoteViewModel.class)) {
            return (T) new NoteViewModel(interactor);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class!");
        }
    }
}
