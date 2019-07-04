package ru.yandex.sharov.example.notes.util;

import android.content.Context;

import androidx.annotation.NonNull;

public class UIUtil {
    public static void assertActivityImplementsInterface(@NonNull Context context, @NonNull Class interfaceClass) {
        if (!(interfaceClass.isAssignableFrom(context.getClass()))) {
            throw new IllegalStateException("Activity " + context.getClass().getSimpleName() + " must implement " + interfaceClass);
        }
    }
}
