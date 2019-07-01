package ru.yandex.sharov.example.notes;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ru.yandex.sharov.example.notes.data.Note;

public class NoteAddOrEditFragment extends Fragment {

    private final String LOG_TAG = "LOG_TAG";

    private TextView tvDate;
    private EditText etTitle;
    private EditText etText;
    private Note note;
    private boolean isAddingNote = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(LOG_TAG, getClass().getSimpleName() + " onCreateView");
        View rootV = inflater.inflate(R.layout.add_or_edit_note_fragment, container, false);
        etText  = rootV.findViewById(R.id.etTextNote);
        etTitle  = rootV.findViewById(R.id.etTitleNote);
        if(note == null ) {
            note = new Note();
        }
        tvDate = rootV.findViewById(R.id.tvDateNote);
        tvDate.setText(note.getDate());
        etTitle.setText(note.getTitle());
        etText.setText(note.getText());
        return rootV;
    }

    public NoteAddOrEditFragment editableMode(boolean addingNote, Note note) {
        isAddingNote = addingNote;
        this.note = note;
        return this;
    }

    @Override
    public void onStop() {
        super.onStop();
        note = null;
    }
}
