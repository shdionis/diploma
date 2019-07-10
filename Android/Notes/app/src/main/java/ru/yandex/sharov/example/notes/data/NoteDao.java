package ru.yandex.sharov.example.notes.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NoteDao {

    @Query("SELECT * FROM note")
    LiveData<List<Note>> getAllNotes();

    @Query("SELECT * FROM note where id = :id")
    Note getNotesById(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdateNotes(Note ... note);

    @Query("DELETE FROM note WHERE id IN(:ids)")
    void deleteNotes(Long[] ids);
}
