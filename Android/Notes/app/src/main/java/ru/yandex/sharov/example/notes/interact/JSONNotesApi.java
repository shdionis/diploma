package ru.yandex.sharov.example.notes.interact;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import ru.yandex.sharov.example.notes.model.RemoteNote;

public interface JSONNotesApi {
    @GET("get")
    Call<List<RemoteNote>> getAllNotes(@Query("version") int version, @Query("name") String name);

    @POST("sync")
    Call<List<RemoteNote>> syncAllNotes(@Field("version") int version,
                                        @Field("user") String user,
                                        @Field("notes") List<RemoteNote> notes);
}
