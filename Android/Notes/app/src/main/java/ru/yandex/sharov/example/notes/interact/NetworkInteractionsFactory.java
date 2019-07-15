package ru.yandex.sharov.example.notes.interact;

import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.yandex.sharov.example.notes.R;
import ru.yandex.sharov.example.notes.model.Note;
import ru.yandex.sharov.example.notes.model.RemoteNote;

public class NetworkInteractionsFactory {


    public static AsyncTask<Void, Void, Void> createGetRemoteNotesTask(LocalRepositoryNoteInteractor dbIteractor, MutableLiveData<Integer> errorData) {
        return new FillLocalStorageFromServerAsyncTask(dbIteractor, errorData);
    }

    private static class FillLocalStorageFromServerAsyncTask extends AsyncTask<Void, Void, Void> {
        private final LocalRepositoryNoteInteractor dbInteractor;
        private final MutableLiveData<Integer> errorData;

        public FillLocalStorageFromServerAsyncTask(LocalRepositoryNoteInteractor dbIteractor, MutableLiveData<Integer> errorData) {
            this.dbInteractor = dbIteractor;
            this.errorData = errorData;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            RemoteNotesServiceInteractor service = RemoteNotesServiceInteractor.getInstance();
            service.getAllNotesApi().getAllNotes(2, "Denis")
                    .enqueue(new Callback<List<RemoteNote>>() {
                        final String LOG_TAG = "[LOG_TAG:retrofit]";

                        @Override
                        public void onResponse(Call<List<RemoteNote>> call, Response<List<RemoteNote>> response) {
                            if (response.body() != null) {
                                List<RemoteNote> remoteNotes = response.body();
                                List<Note> localNotes = new ArrayList<>(remoteNotes.size());
                                for (RemoteNote rnote : remoteNotes) {
                                    localNotes.add(new Note(rnote));
                                }
                                dbInteractor.addOrUpdateNote(localNotes);
                            }
                            errorData.setValue(null);
                        }

                        @Override
                        public void onFailure(Call<List<RemoteNote>> call, Throwable t) {
                            Log.e(LOG_TAG, "Get remote notes failure", t);
                            errorData.setValue(R.string.network_error);
                        }
                    });
            return null;
        }
    }
}
