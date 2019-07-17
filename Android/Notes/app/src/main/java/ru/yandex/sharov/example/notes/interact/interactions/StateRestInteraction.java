package ru.yandex.sharov.example.notes.interact.interactions;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class StateRestInteraction {

    @NonNull
    public static StateRestInteraction createSuccessState() {
        return new StateRestInteraction(Type.SUCCESS);
    }

    @NonNull
    public static StateRestInteraction createErrorState() {
        return new StateRestInteraction(Type.ERROR);
    }

    @NonNull
    private Type type;
    private int code;
    @Nullable
    private Throwable exception;

    public StateRestInteraction(@NonNull Type type) {
        this.type = type;
    }

    @NonNull
    public Type getType() {
        return type;
    }

    public void setType(@NonNull Type type) {
        this.type = type;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Nullable
    public Throwable getException() {
        return exception;
    }

    public void setException(@Nullable Throwable exception) {
        this.exception = exception;
    }

    @NonNull
    public StateRestInteraction withCode(int code) {
        this.code = code;
        return this;
    }

    @NonNull
    public StateRestInteraction withException(@Nullable Throwable exception) {
        this.exception = exception;
        return this;
    }
}
