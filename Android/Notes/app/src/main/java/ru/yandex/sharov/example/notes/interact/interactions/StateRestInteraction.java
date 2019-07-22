package ru.yandex.sharov.example.notes.interact.interactions;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class StateRestInteraction {

    @NonNull
    private Type type;
    private int code;
    @Nullable
    private Throwable exception;

    @NonNull
    public static StateRestInteraction createSuccessState() {
        return new StateRestInteraction(Type.SUCCESS);
    }

    @NonNull
    public static StateRestInteraction createErrorState() {
        return new StateRestInteraction(Type.ERROR);
    }

    public StateRestInteraction(@NonNull Type type) {
        this.type = type;
    }

    @NonNull
    public Type getType() {
        return type;
    }


    public int getCode() {
        return code;
    }

    @Nullable
    public Throwable getException() {
        return exception;
    }

    @NonNull
    public StateRestInteraction withCode(int code) {
        this.code = code;
        return this;
    }

    @NonNull
    public StateRestInteraction withException(@NonNull Throwable exception) {
        this.exception = exception;
        return this;
    }
}
