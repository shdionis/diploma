package ru.yandex.sharov.example.notes;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.yandex.sharov.example.notes.data.Note;

public class NotesRecyclerViewAdapter extends RecyclerView.Adapter {

    private final static String LOG_TAG = "[LOG_TAG:NotRcclrVAdpt]";

    private List<Note> dataList;

    private NoteItemOnClickListener noteItemOnClickListener;

    public void setListener(NoteItemOnClickListener noteItemOnClickListener) {
        this.noteItemOnClickListener = noteItemOnClickListener;
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

    public void setDataList(List<Note> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    private class NoteViewHolder extends RecyclerView.ViewHolder {

        private View noteListItemView;
        private TextView tvTitle;
        private TextView tvDate;
        private TextView tvText;


        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            noteListItemView = itemView;
            tvTitle = noteListItemView.findViewById(R.id.note_item_title);
            tvDate = noteListItemView.findViewById(R.id.note_item_date);
            tvText = noteListItemView.findViewById(R.id.note_item_text);
        }

        public void bindNote(Note n) {
            tvDate.setText(n.getShortDate());
            tvTitle.setText(n.getTitle());
            tvText.setText(n.getText());
            noteListItemView.setOnClickListener(v -> onNoteitemClicked(n.getId()));
        }

        private void onNoteitemClicked(Integer noteId) {
            noteItemOnClickListener.onClickNoteItem(noteId);
        }
    }
}
