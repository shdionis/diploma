package ru.yandex.sharov.example.notes.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import ru.yandex.sharov.example.notes.model.Note;

@Dao
public interface NoteDao {

    @Query("SELECT * FROM notes")
    @NonNull
    LiveData<List<Note>> getAllNotes();

    @Query("SELECT * FROM notes WHERE id = :id")
    @Nullable
    Note getNotesById(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdateNotes(@NonNull Note... note);

    @Query("DELETE FROM notes WHERE id IN(:ids)")
    void deleteNotes(@NonNull Long[] ids);
}
