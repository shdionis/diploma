package ru.yandex.sharov.example.notes.viewmodel.factory;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ru.yandex.sharov.example.notes.interact.LocalRepositoryNoteInteractor;
import ru.yandex.sharov.example.notes.interact.RemoteNotesServiceInteractor;
import ru.yandex.sharov.example.notes.viewmodel.NoteListViewModel;

public class NoteListViewModelFactory implements ViewModelProvider.Factory {

    private static final String LOG_TAG = "[LOG_TAG:NLVMFactory]";
    @NonNull
    private static final String APP_PREFERENCES = "settings";
    @NonNull
    private static final String PREF_USER_KEY = "user";
    @NonNull
    private static final String PREF_VERSION_KEY = "version";
    @NonNull
    private static final String DEFAULT_PREF_USER = "Denis"; //Hardcode
    private static final int DEFAULT_PREF_VERSION = 0;
    @NonNull
    private final LocalRepositoryNoteInteractor dbInteractor;
    @NonNull
    private final RemoteNotesServiceInteractor remoteStorageInteractor;

    public NoteListViewModelFactory(@NonNull Context context) {
        dbInteractor = LocalRepositoryNoteInteractor.getInstance(context);
        SharedPreferences sPref = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        if (!sPref.contains(PREF_USER_KEY)) {
            sPref.edit().putString(PREF_USER_KEY, DEFAULT_PREF_USER).apply();
        }
        if (!sPref.contains(PREF_VERSION_KEY)) {
            sPref.edit().putInt(PREF_VERSION_KEY, DEFAULT_PREF_VERSION).apply();
        }
        String user = sPref.getString(PREF_USER_KEY, DEFAULT_PREF_USER);
        int version = sPref.getInt(PREF_VERSION_KEY, DEFAULT_PREF_VERSION);
        remoteStorageInteractor = RemoteNotesServiceInteractor.getInstance(dbInteractor, user, version);
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        Log.d(LOG_TAG, "create");
        if (modelClass.isAssignableFrom(NoteListViewModel.class)) {
            return (T) new NoteListViewModel(dbInteractor, remoteStorageInteractor);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class!");
        }
    }
}
