package ru.yandex.sharov.example.notes;

import androidx.annotation.NonNull;

interface NoteItemOnClickListenerProvider {
    @NonNull
    NoteItemOnClickListener getListener();
}
