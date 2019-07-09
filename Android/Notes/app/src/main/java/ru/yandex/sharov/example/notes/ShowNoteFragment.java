package ru.yandex.sharov.example.notes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import androidx.lifecycle.ViewModelProviders;

import ru.yandex.sharov.example.notes.util.UIUtil;
import ru.yandex.sharov.example.notes.viewmodel.NoteListViewModelFactory;
import ru.yandex.sharov.example.notes.viewmodel.NoteViewModel;

public class ShowNoteFragment extends Fragment {
    private static final String LOG_TAG = "[LOG_TAG:ShowNote]";
    private static final int DELETE_REQUEST_CODE = 0;
    private static final String NOTE_ARG = "note";

    private NoteItemOnClickListener listener;
    private NoteViewModel noteViewModel;

    @NonNull
    public static ShowNoteFragment newInstance(@NonNull Long noteId) {
        Bundle args = new Bundle();
        args.putLong(NOTE_ARG, noteId.longValue());
        ShowNoteFragment fragment = new ShowNoteFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(LOG_TAG, " onAttach");
        UIUtil.assertContextImplementsInterface(context, NoteItemOnClickListenerProvider.class);
        listener = ((NoteItemOnClickListenerProvider) context).getListener();
        NoteListViewModelFactory factory = new NoteListViewModelFactory(this.requireContext());
        noteViewModel = ViewModelProviders.of(this, factory).get(NoteViewModel.class);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, " onCreate");
        Bundle args = getArguments();
        if (args != null) {
            noteViewModel.getNoteById(args.getLong(NOTE_ARG));
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(LOG_TAG, " onCreateView");
        View rootV = inflater.inflate(R.layout.note_view_fragment, container, false);
        TextView tvDateNote = rootV.findViewById(R.id.note_date);
        TextView tvTitleNote = rootV.findViewById(R.id.note_title);
        TextView tvTextNote = rootV.findViewById(R.id.note_text);
        noteViewModel.getNote().observe(getViewLifecycleOwner(), note -> {
            Log.d(LOG_TAG, "ObserverCallback");
            tvDateNote.setText(note.getLongFormatDate());
            tvTitleNote.setText(note.getTitle());
            tvTextNote.setText(note.getText());
        });
        return rootV;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.note_show_actions_items, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                ConfirmActionDialog.showAlert(getResources().getString(R.string.text_dialog_delete),
                        getResources().getString(R.string.action_delete), this, DELETE_REQUEST_CODE);
                break;
            case R.id.action_edit:
                listener.onEditingNote(noteViewModel.getNote().getValue().getId());
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case DELETE_REQUEST_CODE:
                    noteViewModel.removeNote();
                    listener.onAfterDeleteNote();
                    break;
                default:
                    break;
            }
        }
    }
}
