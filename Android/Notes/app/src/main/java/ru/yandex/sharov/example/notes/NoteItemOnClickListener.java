package ru.yandex.sharov.example.notes;

import androidx.annotation.NonNull;

import java.util.Collections;

import ru.yandex.sharov.example.notes.util.UIUtil;

public interface NoteItemOnClickListener {
    void onClickNoteItem(@NonNull Long noteId);

    void onEditingNote(@NonNull Long noteId);

    void onAddingNote();

    void onAfterChangeNote();

    void onAfterDeleteNote();
}
