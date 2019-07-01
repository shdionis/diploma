package ru.yandex.sharov.example.notes;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ru.yandex.sharov.example.notes.data.DBHelperStub;

public class NotesListFragment extends Fragment {

    private final String LOG_TAG = "LOG_TAG";

    private RecyclerView recyclerView;
    private NotesRecyclerViewAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private NoteItemOnClickListener listener;
    private DBHelperStub dataStub;

    public void setListener(NoteItemOnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataStub = DBHelperStub.getInstance();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(LOG_TAG, getClass().getSimpleName() + " onCreateView");
        View rootV = inflater.inflate(R.layout.notes_list_fragment, container, false);
        recyclerView = rootV.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new NotesRecyclerViewAdapter(dataStub.getData());
        adapter.setListener(listener);
        recyclerView.setAdapter(adapter);
        FloatingActionButton fab = rootV.findViewById(R.id.fab);
        fab.setOnClickListener(v -> {listener.onAddNote(adapter);});
        return rootV;

    }

}
