package ru.yandex.sharov.example.notes;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ru.yandex.sharov.example.notes.data.DBHelperStub;
import ru.yandex.sharov.example.notes.util.UIUtil;

public class NotesListFragment extends Fragment {

    private static final String LOG_TAG = "[LOG_TAG:NoteLstFrgmnt]";

    private RecyclerView recyclerView;
    private NotesRecyclerViewAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private NoteItemOnClickListener listener;

    @NonNull
    public static NotesListFragment newInstance() {
        return new NotesListFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.d(LOG_TAG, " onAttach");
        UIUtil.assertActivityImplementsInterface(context, NoteItemOnClickListenerProvider.class);
        listener = ((NoteItemOnClickListenerProvider) context).getListener();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(LOG_TAG, " onCreateView");
        View rootV = inflater.inflate(R.layout.notes_list_fragment, container, false);
        recyclerView = rootV.findViewById(R.id.recycler_view_note_list);
        layoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new NotesRecyclerViewAdapter();
        adapter.setListener(listener);
        adapter.setDataList(DBHelperStub.getInstance().getData());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL); //TODO: сделать отступ под иконкой
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(adapter);
        FloatingActionButton addNoteFab = rootV.findViewById(R.id.add_note_fab);
        addNoteFab.setOnClickListener(v -> listener.onAddNote());
        return rootV;
    }


}
