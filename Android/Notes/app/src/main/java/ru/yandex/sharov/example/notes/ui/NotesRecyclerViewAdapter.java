package ru.yandex.sharov.example.notes.ui;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

import ru.yandex.sharov.example.notes.NoteItemOnClickListener;
import ru.yandex.sharov.example.notes.R;
import ru.yandex.sharov.example.notes.entities.Note;

public class NotesRecyclerViewAdapter extends RecyclerView.Adapter {

    private static final String LOG_TAG = "[LOG_TAG:NotRcclrVAdpt]";

    @Nullable
    private List<Note> dataList;
    @Nullable
    private NoteItemOnClickListener noteItemOnClickListener;

    public NotesRecyclerViewAdapter() {
        dataList = Collections.emptyList();
    }

    public void setListener(@NonNull NoteItemOnClickListener noteItemOnClickListener) {
        this.noteItemOnClickListener = noteItemOnClickListener;
    }

    public void setDataList(@NonNull List<Note> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(LOG_TAG, " onCreateViewHolder");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_list_item, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.d(LOG_TAG, " onBindViewHolder");
        Note n = dataList.get(position);
        ((NoteViewHolder) holder).bindNote(n);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    private class NoteViewHolder extends RecyclerView.ViewHolder {

        @NonNull
        private View noteListItemView;
        @NonNull
        private TextView tvTitle;
        @NonNull
        private TextView tvDate;
        @NonNull
        private TextView tvText;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            noteListItemView = itemView;
            tvTitle = noteListItemView.findViewById(R.id.note_item_title);
            tvDate = noteListItemView.findViewById(R.id.note_item_date);
            tvText = noteListItemView.findViewById(R.id.note_item_text);
        }

        public void bindNote(@NonNull Note n) {
            tvDate.setText(n.getShortFormatDate());
            tvTitle.setText(n.getTitle());
            tvText.setText(n.getContent());
            noteListItemView.setOnClickListener(v -> onNoteitemClicked(n.getId()));
        }

        private void onNoteitemClicked(@NonNull Long noteId) {
            if (noteItemOnClickListener != null) {
                noteItemOnClickListener.onClickNoteItem(noteId);
            }
        }
    }
}
