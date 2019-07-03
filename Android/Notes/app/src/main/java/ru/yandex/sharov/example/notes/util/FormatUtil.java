package ru.yandex.sharov.example.notes.util;

import java.text.SimpleDateFormat;

public class FormatUtil {
    private static SimpleDateFormat noteDateFormat;
    private static SimpleDateFormat noteDateShortFormat;

    static {
        noteDateFormat = new SimpleDateFormat("dd MM yyyy, HH:mm");
        noteDateShortFormat = new SimpleDateFormat("dd.MM");
    }

    public static String getDateFormated(long time) {
        return noteDateFormat.format(time);
    }

    public static String getShortDateFormated(long time) {
        return noteDateShortFormat.format(time);
    }
}
