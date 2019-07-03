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

    private final static String LOG_TAG = "[LOG_TAG:NotesActivity]";

    private FragmentTransaction fragmentTransaction;
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
            listener = new NoteItemOnClickListener() {
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
                public void onBack() {
                    getSupportFragmentManager().popBackStack();
                }
            };
        }
        return listener;
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
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }


}
