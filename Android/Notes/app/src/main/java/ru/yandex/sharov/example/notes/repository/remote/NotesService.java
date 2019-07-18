package ru.yandex.sharov.example.notes.repository.remote;

import androidx.annotation.NonNull;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NotesService {
    private static int READ_TIMEOUT = 30;
    private static final Object LOCK = new Object();
    private static final String BASE_URL = "http://10.0.2.2:8080/notes-server-1.0-SNAPSHOT/sync/";
    @NonNull
    private static volatile NotesService instance;
    @NonNull
    private Retrofit retrofit;

    private NotesService() {
        Retrofit.Builder builder = new Retrofit.Builder();
        OkHttpClient cleint = new OkHttpClient.Builder()
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .build();
        retrofit = builder.client(cleint).baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
    }

    @NonNull
    public static NotesService getInstance() {
        if (instance == null) {
            synchronized (LOCK) {
                if (instance == null) {
                    instance = new NotesService();
                }
            }
        }
        return instance;
    }

    @NonNull
    public NotesServiceApi getAllNotesApi() {
        return retrofit.create(NotesServiceApi.class);
    }
}
