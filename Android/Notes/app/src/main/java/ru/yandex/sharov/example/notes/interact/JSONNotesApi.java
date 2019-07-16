package ru.yandex.sharov.example.notes.interact;

import androidx.annotation.NonNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import ru.yandex.sharov.example.notes.model.RemoteNote;

public interface JSONNotesApi {
    @GET("get")
    @NonNull
    Call<List<RemoteNote>> getAllNotes(@NonNull @Query("version") int version, @NonNull @Query("name") String name);

    @POST("sync")
    @NonNull
    Call<List<RemoteNote>> syncAllNotes(@NonNull @Field("version") int version,
                                        @NonNull @Field("user") String user,
                                        @NonNull @Field("notes") List<RemoteNote> notes);
}
