package ru.yandex.sharov.example.notes;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class NotesActivity extends AppCompatActivity implements NotesListFragment.OnNoteSelectedListener {

    private final String LOG_TAG = "LOG_TAG";

    private Fragment noteListFrag;
    private ShowNoteFragent showNoteFrag;
    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        Log.d(LOG_TAG, getClass().getSimpleName() + " onCreate");
        noteListFrag = new NotesListFragment();
        showNoteFrag = new ShowNoteFragent();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frameLayout, noteListFrag);
        fragmentTransaction.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(LOG_TAG, getClass().getSimpleName() + " onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LOG_TAG, getClass().getSimpleName() + " onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(LOG_TAG, getClass().getSimpleName() + " onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(LOG_TAG, getClass().getSimpleName() + " onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, getClass().getSimpleName() + " onDestroy");
    }


    @Override
    public void onNoteSelect(Note note) {
        Log.d(LOG_TAG, getClass().getSimpleName() + " onNoteSelect" + note.toString());
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, showNoteFrag.withNote(note));
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
