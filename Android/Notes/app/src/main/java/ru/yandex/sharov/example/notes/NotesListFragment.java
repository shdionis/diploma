package ru.yandex.sharov.example.notes;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NotesListFragment extends Fragment {
    private OnNoteSelectedListener onNoteSelectedListener;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Note> data;
    private final String LOG_TAG = "LOG_TAG";

    public interface OnNoteSelectedListener {
        void onNoteSelect(Note note);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onNoteSelectedListener = (OnNoteSelectedListener) context;
        } catch (ClassCastException ex) {
            throw new ClassCastException(context.toString() + " Activity for tis fragment must be NotesListFragment.OnNoteSelectedListener interface implementation!");
        }
        Log.d(LOG_TAG, getClass().getSimpleName() + " onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, getClass().getSimpleName() + " onCreate");

        data = new ArrayList<>();
        for (int i = 1; i <= 50; i++) {
            StringBuilder textNote = new StringBuilder();
            for (int j = 0; j < 30; j++) {
                textNote.append("Note").append(i).append(j);
            }

            data.add(new Note("Note"+i, System.currentTimeMillis()-i, textNote.toString()));
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(LOG_TAG, getClass().getSimpleName() + " onCreateView");
        View rootV = inflater.inflate(R.layout.notes_list_fragment, container, false);
        if(recyclerView == null) {
            recyclerView = rootV.findViewById(R.id.recyclerView);
            layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);
        }
        adapter = new NotesRecyclerViewAdapter(data, this);
        recyclerView.setAdapter(adapter);

        return rootV;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(LOG_TAG, getClass().getSimpleName() + " onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(LOG_TAG, getClass().getSimpleName() + " onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(LOG_TAG, getClass().getSimpleName() + " onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(LOG_TAG, getClass().getSimpleName() + " onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(LOG_TAG, getClass().getSimpleName() + " onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(LOG_TAG, getClass().getSimpleName() + " onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, getClass().getSimpleName() + " onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(LOG_TAG, getClass().getSimpleName() + " onDetach");
    }

    public void showNote(Note note) {
        onNoteSelectedListener.onNoteSelect(note);
        Log.d(LOG_TAG, getClass().getSimpleName() + " showNote");
    }
}
