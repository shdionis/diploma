package ru.yandex.sharov.example.notes.util;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;

public class FormatUtil {
    private static final SimpleDateFormat noteDateFormat;
    private static final SimpleDateFormat noteDateShortFormat;

    static {
        noteDateFormat = new SimpleDateFormat("dd MM yyyy, HH:mm");
        noteDateShortFormat = new SimpleDateFormat("dd.MM");
    }

    @NonNull
    public static String getDateFormated(long time) {
        return noteDateFormat.format(time);
    }

    @NonNull
    public static String getShortDateFormated(long time) {
        return noteDateShortFormat.format(time);
    }
}
