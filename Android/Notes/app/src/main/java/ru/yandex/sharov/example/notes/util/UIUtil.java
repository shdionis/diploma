package ru.yandex.sharov.example.notes.util;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;

import java.util.Comparator;

import ru.yandex.sharov.example.notes.model.Note;

public class UIUtil {

    public static final Comparator<Note> ASC_NOTE_COMPARATOR = (Note n1, Note n2) -> Long.compare(n1.getDate(), n2.getDate()) * -1;
    public static final Comparator<Note> DESC_NOTE_COMPARATOR = (Note n1, Note n2) -> Long.compare(n1.getDate(), n2.getDate());

    public static void assertContextImplementsInterface(@NonNull Context context, @NonNull Class interfaceClass) {
        if (!(interfaceClass.isAssignableFrom(context.getClass()))) {
            throw new IllegalStateException("Context must implement " + interfaceClass);
        }
    }

    public static void hideKeyTool(@NonNull Activity activity) {
        if (activity.getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }
}
