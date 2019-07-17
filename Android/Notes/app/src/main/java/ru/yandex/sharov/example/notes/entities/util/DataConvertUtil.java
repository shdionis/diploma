package ru.yandex.sharov.example.notes.entities.util;

import androidx.annotation.NonNull;

import ru.yandex.sharov.example.notes.entities.Note;
import ru.yandex.sharov.example.notes.entities.RemoteNote;

public class DataConvertUtil {
    @NonNull
    public static RemoteNote convertNoteToRemoteNote(@NonNull Note note) {
        return new RemoteNote(note.getGuid(), note.getTitle(), note.getContent(), note.getDate(), note.getDeleted());
    }

    @NonNull
    public static Note convertRemoteNoteToNote(@NonNull RemoteNote remoteNote) {
        return new Note(remoteNote.getGuid(), remoteNote.getTitle(), remoteNote.getContent(), remoteNote.getDate(), remoteNote.getDeleted());
    }
}
