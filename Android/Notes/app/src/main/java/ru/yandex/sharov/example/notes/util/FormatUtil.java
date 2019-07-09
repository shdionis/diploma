package ru.yandex.sharov.example.notes.util;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class FormatUtil {
    private static final SimpleDateFormat NOTE_DATE_FORMAT;
    private static final SimpleDateFormat NOTE_DATE_SHORT_FORMAT;

    static {
        NOTE_DATE_FORMAT = new SimpleDateFormat("dd MM yyyy, HH:mm");
        NOTE_DATE_SHORT_FORMAT = new SimpleDateFormat("dd.MM");
    }

    @NonNull
    public static String getDateFormated(long time) {
        return NOTE_DATE_FORMAT.format(time);
    }

    @NonNull
    public static String getShortDateFormated(long time) {
        return NOTE_DATE_SHORT_FORMAT.format(time);
    }
}
