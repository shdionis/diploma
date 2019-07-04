package ru.yandex.sharov.example.notes;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class NotesActivity extends AppCompatActivity implements NoteItemOnClickListenerProvider, FragmentManager.OnBackStackChangedListener {

    private static final String LOG_TAG = "[LOG_TAG:NotesActivity]";

    private NoteItemOnClickListener listener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        Log.d(LOG_TAG, " onCreate");
        if (savedInstanceState == null) {
            NotesListFragment noteListFragment = NotesListFragment.newInstance();
            replaceFragment(noteListFragment, false);
        }
        getSupportFragmentManager().addOnBackStackChangedListener(this);
        shouldShowBackButton();
    }

    @NonNull
    @Override
    public NoteItemOnClickListener getListener() {
        if (listener == null) {
            listener = createListener();
        }
        return listener;
    }

    @NonNull
    private NoteItemOnClickListener createListener() {
        return new NoteItemOnClickListener() {
            @Override
            public void onClickNoteItem(int noteId) {
                ShowNoteFragment showNoteFragment = ShowNoteFragment.newInstance(noteId);
                replaceFragment(showNoteFragment, true);
            }

            @Override
            public void onAddNote() {
                NoteAddOrEditFragment addOrEditFragment = NoteAddOrEditFragment.newInstance();
                replaceFragment(addOrEditFragment, true);
            }

            @Override
            public void onEditNote(int noteId) {
                NoteAddOrEditFragment addOrEditFragment = NoteAddOrEditFragment.newInstance(noteId);
                replaceFragment(addOrEditFragment, true);
            }

            @Override
            public void onAfterChangedNote() {
                getSupportFragmentManager().popBackStack();
            }
        };
    }

    @Override
    public void onBackStackChanged() {
        shouldShowBackButton();
    }

    private void shouldShowBackButton() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(getSupportFragmentManager().getBackStackEntryCount() > 0);
    }

    @Override
    public boolean onSupportNavigateUp() {
        getSupportFragmentManager().popBackStack();
        return super.onSupportNavigateUp();
    }

    private void replaceFragment(@NonNull Fragment fragment, boolean addToBackStack) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }


}
