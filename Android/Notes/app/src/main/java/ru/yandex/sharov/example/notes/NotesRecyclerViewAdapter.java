package ru.yandex.sharov.example.notes;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import ru.yandex.sharov.example.notes.data.Note;

public class NotesRecyclerViewAdapter extends RecyclerView.Adapter {

    private List<Note> dataList;
    private final String LOG_TAG = "LOG_TAG";

    protected NoteItemOnClickListener noteItemOnClickListener;

    public void setListener(NoteItemOnClickListener noteItemOnClickListener) {
        this.noteItemOnClickListener = noteItemOnClickListener;
    }

    public NotesRecyclerViewAdapter(List<Note> dataSource){
        dataList = dataSource;
        Log.d(LOG_TAG, getClass().getSimpleName() + " constructor");
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(LOG_TAG, getClass().getSimpleName() + " onCreateViewHolder");
        return new NoteViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.note_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.d(LOG_TAG, getClass().getSimpleName() + " onBindViewHolder");
        View ll = ((NoteViewHolder)holder).getNoteListItemView();
        Note n = dataList.get(position);
        TextView tvTitle = ll.findViewById(R.id.tvTitle);
        TextView tvDate = ll.findViewById(R.id.tvDate);
        TextView tvText = ll.findViewById(R.id.tvText);

        tvDate.setText(n.getShortDate());
        tvTitle.setText(n.getTitle());
        tvText.setText(n.getShortText(40));
        ((NoteViewHolder) holder).bindNote(n);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    private class NoteViewHolder extends RecyclerView.ViewHolder {

        private View noteListItemView;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            noteListItemView = itemView;
        }

        public View getNoteListItemView() {
            return noteListItemView;
        }

        public void bindNote(Note note) {
            noteListItemView.setOnClickListener(v -> onNoteitemClicked(note));
        }

        private void onNoteitemClicked(Note note) {
            noteItemOnClickListener.onClickNoteItem(note);
        }
    }
}
