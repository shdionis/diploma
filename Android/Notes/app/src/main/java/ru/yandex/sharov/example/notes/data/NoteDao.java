package ru.yandex.sharov.example.notes.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NoteDao {

    @Query("SELECT * FROM note")
    @NonNull
    LiveData<List<Note>> getAllNotes();

    @Query("SELECT * FROM note WHERE id = :id")
    @Nullable
    Note getNotesById(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdateNotes(@NonNull Note ... note);

    @Query("DELETE FROM note WHERE id IN(:ids)")
    void deleteNotes(@NonNull Long[] ids);
}
