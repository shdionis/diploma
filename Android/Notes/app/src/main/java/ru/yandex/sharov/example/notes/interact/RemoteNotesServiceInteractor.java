package ru.yandex.sharov.example.notes.interact;

import androidx.annotation.NonNull;
import androidx.core.util.Consumer;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.yandex.sharov.example.notes.interact.interactions.NetworkInteractionsFactory;
import ru.yandex.sharov.example.notes.interact.interactions.StateRestInteraction;

public class RemoteNotesServiceInteractor {
    private static int READ_TIMEOUT = 30;
    @NonNull
    private static final Object LOCK = new Object();
    @NonNull
    private static final String BASE_URL = "http://10.0.2.2:8080/notes-server-1.0-SNAPSHOT/sync/";
    @NonNull
    private static RemoteNotesServiceInteractor instance;
    @NonNull
    private final LocalRepositoryNoteInteractor dbInteractor;
    @NonNull
    private Retrofit retrofit;
    @NonNull
    private String user;
    private int version;

    public RemoteNotesServiceInteractor(@NonNull LocalRepositoryNoteInteractor dbInteractor, String user, int version) {
        Retrofit.Builder builder = new Retrofit.Builder();
        OkHttpClient cleint = new OkHttpClient.Builder()
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .build();
        retrofit = builder.client(cleint).baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        this.dbInteractor = dbInteractor;
        this.user = user;
        this.version = version;
    }

    @NonNull
    public static RemoteNotesServiceInteractor getInstance(LocalRepositoryNoteInteractor dbInteractor, String user, int version) {
        if (instance == null) {
            synchronized (LOCK) {
                if (instance == null) {
                    instance = new RemoteNotesServiceInteractor(dbInteractor, user, version);
                }
            }
        }
        return instance;
    }

    @NonNull
    public String getUser() {
        return user;
    }

    public int getVersion() {
        return version;
    }

    public void pullDataToLocalStorage(Consumer<StateRestInteraction> consumer) {
        NetworkInteractionsFactory.executeGetRemoteNotesTask(
                getAllNotesApi().getAllNotes(getVersion(), getUser()),
                dbInteractor,
                consumer
        );

    }

    @NonNull
    public JSONNotesApi getAllNotesApi() {
        return retrofit.create(JSONNotesApi.class);
    }
}
