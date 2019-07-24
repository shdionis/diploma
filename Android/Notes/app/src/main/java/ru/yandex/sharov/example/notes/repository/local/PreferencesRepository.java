package ru.yandex.sharov.example.notes.repository.local;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

public class PreferencesRepository {
    private static final Object LOCK = new Object();
    private static final String APP_PREFERENCES = "settings";
    private static final String PREF_USER_KEY = "user";
    private static final String PREF_VERSION_KEY = "version";
    private static final String DEFAULT_PREF_USER = "Denis"; //Hardcode
    private static final int DEFAULT_PREF_VERSION = 0;
    @NonNull
    private static volatile PreferencesRepository instance;
    @NonNull
    private SharedPreferences sPref;

    @NonNull
    public static PreferencesRepository getInstance(@NonNull Context context) {
        if (instance == null) {
            synchronized (LOCK) {
                if (instance == null) {
                    instance = new PreferencesRepository(context);
                }
                return instance;
            }
        }
        return instance;
    }

    private PreferencesRepository(@NonNull Context context) {
        this.sPref = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        if (!sPref.contains(PREF_USER_KEY)) {
            sPref.edit().putString(PREF_USER_KEY, DEFAULT_PREF_USER).apply();
        }
        if (!sPref.contains(PREF_VERSION_KEY)) {
            sPref.edit().putInt(PREF_VERSION_KEY, DEFAULT_PREF_VERSION).apply();
        }
    }

    @NonNull
    public String getUser() {
        return sPref.getString(PREF_USER_KEY, DEFAULT_PREF_USER);
    }

    public void setUser(@NonNull String user) {
        sPref.edit().putString(PREF_USER_KEY, user).apply();
    }

    public int getVersion() {
        return sPref.getInt(PREF_VERSION_KEY, DEFAULT_PREF_VERSION);
    }

    public void setVersion(int version) {
        sPref.edit().putInt(PREF_VERSION_KEY, version).apply();
    }


}
