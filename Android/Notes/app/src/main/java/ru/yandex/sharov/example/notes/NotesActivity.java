package ru.yandex.sharov.example.notes;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import ru.yandex.sharov.example.notes.data.Note;

public class NotesActivity extends AppCompatActivity {

    private final String LOG_TAG = "LOG_TAG";

    private Fragment noteListFragment;
    private ShowNoteFragment showNoteFragment;
    private NoteAddOrEditFragment addOrEditFragment;
    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        Log.d(LOG_TAG, getClass().getSimpleName() + " onCreate");
        noteListFragment = new NotesListFragment();
        showNoteFragment = new ShowNoteFragment();
        addOrEditFragment = new NoteAddOrEditFragment();
        NoteItemOnClickListener listener = new NoteItemOnClickListener() {
            @Override
            public void onClickNoteItem(@NonNull Note note) {
                Log.d(LOG_TAG, getClass().getSimpleName() + " onNoteSelect" + note.toString());
                replaceFragment(showNoteFragment.withNote(note), true);
            }

            @Override
            public void onAddNote(NotesRecyclerViewAdapter adapter) {
                Log.d(LOG_TAG, getClass().getSimpleName() + " onAddNote");
                replaceFragment(addOrEditFragment, true);
            }
        };
        ((NotesListFragment) noteListFragment).setListener(listener);
        replaceFragment(noteListFragment, false);

    }

    private void replaceFragment(Fragment fragment, boolean addToBackStack) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        if(addToBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }
}
