package ru.yandex.sharov.example.notes;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ru.yandex.sharov.example.notes.data.Note;

public class ShowNoteFragment extends Fragment {
    private Note note;
    private TextView tvDateNote;
    private TextView tvTitleNote;
    private TextView tvTextNote;

    private final String LOG_TAG = "LOG_TAG";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(LOG_TAG, getClass().getSimpleName() + " onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, getClass().getSimpleName() + " onCreate");

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(LOG_TAG, getClass().getSimpleName() + " onCreateView");
        View rootV = inflater.inflate(R.layout.note_view_fragment, container, false);
        tvDateNote = rootV.findViewById(R.id.tvDateNote);
        tvTitleNote = rootV.findViewById(R.id.tvTitleNote);
        tvTextNote = rootV.findViewById(R.id.tvTextNote);
        tvDateNote.setText(note.getDate());
        tvTitleNote.setText(note.getTitle());
        tvTextNote.setText(note.getText());

        return rootV;
    }

    public ShowNoteFragment withNote(Note note) {
        this.note = note;
        return this;
    }
}
