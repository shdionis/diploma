package ru.yandex.sharov.example.notes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ru.yandex.sharov.example.notes.util.UIBehaviorHandlerFactory;
import ru.yandex.sharov.example.notes.util.UIUtil;
import ru.yandex.sharov.example.notes.viewmodel.NoteListViewModel;
import ru.yandex.sharov.example.notes.viewmodel.NoteListViewModelFactory;

public class NotesListFragment extends Fragment {

    private static final String LOG_TAG = "[LOG_TAG:NoteLstFrgmnt]";

    private NotesRecyclerViewAdapter adapter;
    private NoteListViewModel noteListViewModel;
    private NoteItemOnClickListener listener;

    @NonNull
    public static NotesListFragment newInstance() {
        return new NotesListFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.d(LOG_TAG, " onAttach");
        UIUtil.assertContextImplementsInterface(context, NoteItemOnClickListenerProvider.class);
        listener = ((NoteItemOnClickListenerProvider) context).getListener();
    }

    @SuppressLint("RestrictedApi")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(LOG_TAG, " onCreateView");
        View rootV = inflater.inflate(R.layout.notes_list_fragment, container, false);
        noteListViewModel = ViewModelProviders.of(this, new NoteListViewModelFactory(requireContext())).get(NoteListViewModel.class);
        FloatingActionButton addNoteFab = rootV.findViewById(R.id.add_note_fab);
        addNoteFab.setOnClickListener(v -> listener.onAddingNote());
        noteListViewModel.isShowProgressBar().observe(this, progressShowFlag -> {
            if (progressShowFlag) {
                rootV.findViewById(R.id.progress_bar_load_data).setVisibility(View.INVISIBLE);
                rootV.findViewById(R.id.note_list_views_container).setVisibility(View.VISIBLE);
                initNoteListRecyclerView(rootV);
                initBottomSheet(rootV, addNoteFab);
            }
        });

        return rootV;
    }

    private void initBottomSheet(@NonNull View rootV, @NonNull FloatingActionButton fab) {
        View bottomSheet = rootV.findViewById(R.id.bottom_sheet);
        bottomSheet.setVisibility(View.VISIBLE);
        View closeOpenBtn = rootV.findViewById(R.id.bottom_sheet_close_open_btn);
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setBottomSheetCallback(
                UIBehaviorHandlerFactory.createBottomSheetCallback(fab, closeOpenBtn)
        );
        closeOpenBtn.setOnClickListener(
                UIBehaviorHandlerFactory.createCloseOpenBtnOnClickListener(bottomSheetBehavior)
        );
        ToggleButton dateSortBtn = bottomSheet.findViewById(R.id.date_sort_toggle);
        dateSortBtn.setOnCheckedChangeListener(UIBehaviorHandlerFactory.createOnCheckedChangeListener(noteListViewModel));
        EditText searchEditText = rootV.findViewById(R.id.search_edit_text);
        searchEditText.addTextChangedListener(UIBehaviorHandlerFactory.createTextChangedListener(noteListViewModel));
    }

    private void initNoteListRecyclerView(@NonNull View rootV) {
        RecyclerView recyclerView = rootV.findViewById(R.id.recycler_view_note_list);
        recyclerView.setVisibility(View.VISIBLE);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new NotesRecyclerViewAdapter();
        adapter.setListener(listener);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL); //TODO: сделать отступ под иконкой
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(adapter);
        noteListViewModel.getData().observe(getViewLifecycleOwner(), notes -> {
            adapter.setDataList(notes);
        });
    }
}
