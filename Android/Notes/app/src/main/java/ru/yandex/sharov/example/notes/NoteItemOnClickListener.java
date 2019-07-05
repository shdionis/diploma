package ru.yandex.sharov.example.notes;

import java.util.Collections;

import ru.yandex.sharov.example.notes.util.UIUtil;

public interface NoteItemOnClickListener {
    void onClickNoteItem(int noteId);

    void onEditingNote(int noteId);

    void onAddingNote();

    void onAfterChangeNote();

    void onAfterDeleteNote();
}
