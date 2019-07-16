package ru.yandex.sharov.example.notes.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import ru.yandex.sharov.example.notes.model.Note;

@Dao
public interface NoteDao {

    @Query("SELECT * FROM notes")
    @NonNull
    LiveData<List<Note>> getAllNotes();

    @Query("SELECT * FROM notes")
    @NonNull
    List<Note> getAllNotesList();

    @Query("SELECT * FROM notes WHERE id = :id")
    @Nullable
    Note getNotesById(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdateNotes(@NonNull Iterable<Note> note);

    @Query("DELETE FROM notes WHERE id IN(:ids)")
    void deleteNotesByIds(@NonNull Long[] ids);

    @Query("DELETE FROM notes WHERE guid IN(:ids)")
    void deleteNotesByUIDs(@NonNull Set<String> ids);

}
