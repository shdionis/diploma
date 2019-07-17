package ru.yandex.sharov.example.notes.interact.interactions;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.util.Consumer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;
import ru.yandex.sharov.example.notes.entities.Note;
import ru.yandex.sharov.example.notes.entities.RemoteNote;
import ru.yandex.sharov.example.notes.entities.util.DataConvertUtil;
import ru.yandex.sharov.example.notes.interact.NotesUseCases;

public class NetworkInteractionsFactory {

    public static ExecutorService executeGetRemoteNotesTask(Call<List<RemoteNote>> remoteCall, NotesUseCases interactor, Consumer<StateRestInteraction> errorDataConsumer) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(new GetNotesRunnable(remoteCall, interactor, errorDataConsumer));
        return executorService;
    }

    private static class GetNotesRunnable implements Runnable {
        @NonNull
        private final Call<List<RemoteNote>> remoteCall;
        @NonNull
        private final NotesUseCases interactor;
        @NonNull
        private final Consumer<StateRestInteraction> errorData;

        public GetNotesRunnable(@NonNull Call<List<RemoteNote>> remoteCall, @NonNull NotesUseCases interactor, @NonNull Consumer<StateRestInteraction> errorData) {
            this.remoteCall = remoteCall;
            this.interactor = interactor;
            this.errorData = errorData;
        }

        @Override
        public void run() {
            remoteCall.enqueue(new Callback<List<RemoteNote>>() {
                @NonNull
                private static final String LOG_TAG = "[LOG_TAG:retrofit]";

                @Override
                public void onResponse(Call<List<RemoteNote>> call, Response<List<RemoteNote>> response) {
                    StateRestInteraction state;
                    if (response.body() != null && response.isSuccessful()) {
                        List<RemoteNote> remoteNotes = response.body();
                        List<Note> localNotes = new ArrayList<>(remoteNotes.size());
                        for (RemoteNote rnote : remoteNotes) {
                            localNotes.add(DataConvertUtil.convertRemoteNoteToNote(rnote));
                        }
                        interactor.addOrUpdateNotes(localNotes);
                        state = StateRestInteraction.createSuccessState();
                    } else {
                        state = StateRestInteraction.createErrorState()
                                .withException(new HttpException(response))
                                .withCode(response.code());
                    }
                    errorData.accept(state);
                }

                @Override
                public void onFailure(Call<List<RemoteNote>> call, Throwable t) {
                    Log.e(LOG_TAG, "Get remote notes failure", t);
                    errorData.accept(StateRestInteraction.createErrorState()
                            .withException(t)
                    );
                }
            });
        }
    }
}
