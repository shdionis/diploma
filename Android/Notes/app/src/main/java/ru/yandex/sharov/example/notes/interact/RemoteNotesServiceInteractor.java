package ru.yandex.sharov.example.notes.interact;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RemoteNotesServiceInteractor {

    private static final Object LOCK = new Object();
    private static final String BASE_URL = "http://10.0.2.2:8080/notes-server-1.0-SNAPSHOT/sync/";
    private static RemoteNotesServiceInteractor instance;

    private Retrofit retrofit;

    public RemoteNotesServiceInteractor() {
        Retrofit.Builder builder = new Retrofit.Builder();
        retrofit = builder.baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
    }

    public static RemoteNotesServiceInteractor getInstance() {
        if(instance==null) {
            synchronized (LOCK) {
                if(instance==null) {
                    instance = new RemoteNotesServiceInteractor();
                }
            }
        }
        return instance;
    }

    public JSONNotesApi getAllNotesApi() {
        return retrofit.create(JSONNotesApi.class);
    }
}
