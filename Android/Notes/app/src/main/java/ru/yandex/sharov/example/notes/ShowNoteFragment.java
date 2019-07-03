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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ru.yandex.sharov.example.notes.data.DBHelperStub;
import ru.yandex.sharov.example.notes.data.Note;

public class ShowNoteFragment extends Fragment {
    private final String LOG_TAG = "LOG_TAG";
    private final String ENTITY_NAME = "ShowNoteFragment";

    @NonNull
    private Note note;
    private NoteItemOnClickListener listener;

    public static ShowNoteFragment newInstance(@NonNull Integer noteId) {
        Bundle args = new Bundle();
        args.putInt("note", noteId);
        ShowNoteFragment fragment = new ShowNoteFragment();
        fragment.setArguments(args);
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
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            note = DBHelperStub.getInstance().getNoteById(savedInstanceState.getInt("note"));
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, ENTITY_NAME + " onCreate");
        Bundle args = getArguments();
        if (args != null) {
            this.note = DBHelperStub.getInstance().getNoteById(args.getInt("note"));
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(LOG_TAG, ENTITY_NAME + " onCreateView");
        View rootV = inflater.inflate(R.layout.note_view_fragment, container, false);
        TextView tvDateNote = rootV.findViewById(R.id.note_date);
        TextView tvTitleNote = rootV.findViewById(R.id.note_title);
        TextView tvTextNote = rootV.findViewById(R.id.note_text);

        tvDateNote.setText(note.getDate());
        tvTitleNote.setText(note.getTitle());
        tvTextNote.setText(note.getText());
        return rootV;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("note", note.getId());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.note_show_actions_items, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                DBHelperStub.getInstance().removeNote(note.getId());
                listener.onBack();
                break;
            case R.id.action_edit:
                listener.onEditNote(note.getId());
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
