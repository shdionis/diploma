package ru.yandex.sharov.example.notes.ui;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import ru.yandex.sharov.example.notes.NoteItemOnClickListener;
import ru.yandex.sharov.example.notes.NoteItemOnClickListenerProvider;
import ru.yandex.sharov.example.notes.R;
import ru.yandex.sharov.example.notes.databinding.AddOrEditNoteFragmentBinding;
import ru.yandex.sharov.example.notes.util.UIUtil;
import ru.yandex.sharov.example.notes.viewmodel.NoteViewModel;
import ru.yandex.sharov.example.notes.viewmodel.factory.NoteViewModelFactory;

public class NoteAddOrEditFragment extends Fragment {

    private static final String LOG_TAG = "[LOG_TAG:NtAOEFrgmt]";
    private static final int SAVE_CHANGES_REQUEST_CODE = 1;
    private static final String NOTE_ID_ARG = "noteId";

    private NoteItemOnClickListener listener;
    private NoteViewModel noteViewModel;
    private boolean isNewNote = false;

    @NonNull
    public static NoteAddOrEditFragment newInstance(@NonNull Long noteId) {
        Bundle args = new Bundle();
        args.putLong(NOTE_ID_ARG, noteId);
        NoteAddOrEditFragment fragment = new NoteAddOrEditFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    public static NoteAddOrEditFragment newInstance() {
        return new NoteAddOrEditFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.d(LOG_TAG, " onAttach");
        UIUtil.assertContextImplementsInterface(context, NoteItemOnClickListenerProvider.class);
        listener = ((NoteItemOnClickListenerProvider) context).getListener();
        noteViewModel = ViewModelProviders.of(
                this,
                new NoteViewModelFactory(requireContext().getApplicationContext())
        ).get(NoteViewModel.class);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Log.d(LOG_TAG, " onCreate");
        Bundle args = getArguments();
        if (args != null) {
            Log.d(LOG_TAG, " onCreate-fromArgs");
            noteViewModel.getNoteById(args.getLong(NOTE_ID_ARG));
        } else {
            noteViewModel.getNoteById(null);
            isNewNote = true;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(LOG_TAG, " onCreateView");
        AddOrEditNoteFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.add_or_edit_note_fragment, container, false);
        binding.setModel(noteViewModel);
        View rootV = binding.getRoot();
        return rootV;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.note_edit_actions_items, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        UIUtil.hideKeyTool(requireActivity());
        switch (item.getItemId()) {
            case R.id.action_save:
                if (!isNewNote) {
                    ConfirmActionDialog.showAlert(
                            getResources().getString(R.string.text_dialog_save),
                            getResources().getString(R.string.action_save),
                            this,
                            SAVE_CHANGES_REQUEST_CODE
                    );
                } else {
                    saveNote();
                }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveNote() {
        noteViewModel.addOrUpdateNote();
        listener.onAfterChangeNote();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case SAVE_CHANGES_REQUEST_CODE:
                    saveNote();
                    break;
                default:
                    break;
            }
        }
    }
}
