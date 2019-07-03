package ru.yandex.sharov.example.notes;

public interface NoteItemOnClickListener {
    void onClickNoteItem(int noteId);

    void onEditNote(int noteId);

    void onAddNote();

    void onBack();
}
