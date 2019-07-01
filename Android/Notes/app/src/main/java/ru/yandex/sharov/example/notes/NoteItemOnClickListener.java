package ru.yandex.sharov.example.notes;

import androidx.annotation.NonNull;

import ru.yandex.sharov.example.notes.data.Note;

public interface NoteItemOnClickListener {
    void onClickNoteItem(@NonNull Note note);

    void onAddNote(NotesRecyclerViewAdapter adapter);
}