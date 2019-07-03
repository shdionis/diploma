package ru.yandex.sharov.example.notes;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ru.yandex.sharov.example.notes.data.DBHelperStub;
import ru.yandex.sharov.example.notes.data.Note;

public class NoteAddOrEditFragment extends Fragment {

    private final static String LOG_TAG = "[LOG_TAG:NtAOEFrgmt]";

    private TextView noteDate;
    private EditText noteTitle;
    private EditText noteText;
    private Note note;
    private NoteItemOnClickListener listener;

    public static NoteAddOrEditFragment newInstance(int noteId) {
        Bundle args = new Bundle();
        args.putInt("noteId", noteId);
        NoteAddOrEditFragment fragment = new NoteAddOrEditFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static NoteAddOrEditFragment newInstance() {
        NoteAddOrEditFragment fragment = new NoteAddOrEditFragment();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(LOG_TAG, " onAttach");
        try {
            listener = ((NoteItemOnClickListenerProvider) context).getListener();
        } catch (ClassCastException ex) {
            throw new RuntimeException("Context must be implementation of NoteItemOnClickListenerProvider!", ex);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, " onCreate");
        Bundle args = getArguments();
        if (args != null) {
            Log.d(LOG_TAG, " onCreate-fromArgs");
            note = DBHelperStub.getInstance().getNoteById(args.getInt("noteId"));
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(LOG_TAG, " onCreateView");
        View rootV = inflater.inflate(R.layout.add_or_edit_note_fragment, container, false);
        noteText = rootV.findViewById(R.id.note_edit_text);
        noteTitle = rootV.findViewById(R.id.note_edit_title);
        noteDate = rootV.findViewById(R.id.note_date);
        if (note == null) {
            note = new Note();
        }
        noteDate.setText(note.getDate());
        if (savedInstanceState == null) {
            noteTitle.setText(note.getTitle());
            noteText.setText(note.getText());
        }
        return rootV;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, " onDestroy");
        note = null;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(LOG_TAG, " onSaveInstanceState");
        outState.putInt("noteId", note.getId());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.note_edit_actions_items, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                note.setDate(System.currentTimeMillis());
                note.setTitle(noteTitle.getText().toString());
                note.setText(noteText.getText().toString());
                DBHelperStub.getInstance().addOrUpdateNote(note);
                listener.onBack();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
