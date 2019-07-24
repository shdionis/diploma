package ru.yandex.sharov.example.notes;

import androidx.annotation.NonNull;

public interface NoteItemOnClickListenerProvider {
    @NonNull
    NoteItemOnClickListener getListener();
}
