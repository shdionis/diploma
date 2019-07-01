package ru.yandex.sharov.example.notes;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NotesRecyclerViewAdapter extends RecyclerView.Adapter {
    private NotesListFragment parent;
    private List<Note> dataList;
    private final String LOG_TAG = "LOG_TAG";

    public NotesRecyclerViewAdapter(List<Note> dataSource, NotesListFragment notesListFragment){
        dataList = dataSource;
        parent = notesListFragment;
        Log.d(LOG_TAG, getClass().getSimpleName() + " constructor");
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(LOG_TAG, getClass().getSimpleName() + " onCreateViewHolder");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_list_item, parent, false);
        NoteViewHolder nvh = new NoteViewHolder(view);
        return nvh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.d(LOG_TAG, getClass().getSimpleName() + " onBindViewHolder");
        View ll = ((NoteViewHolder)holder).getTitle();
        Note n = dataList.get(position);
        TextView tvTitle = ll.findViewById(R.id.tvTitle);
        TextView tvDate = ll.findViewById(R.id.tvDate);
        TextView tvText = ll.findViewById(R.id.tvText);

        tvDate.setText(n.getShortDate());
        tvTitle.setText(n.getTitle());
        tvText.setText(n.getShortText(40));
        ll.setOnClickListener(new NoteItemOnClickListener(n, parent));
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    private static class NoteViewHolder extends RecyclerView.ViewHolder {

        private View title;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView;
        }

        public View getTitle() {
            return title;
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        Log.d(LOG_TAG, getClass().getSimpleName() + " onAttachedToRecyclerView");
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        Log.d(LOG_TAG, getClass().getSimpleName() + " onDetachedFromRecyclerView");
        super.onDetachedFromRecyclerView(recyclerView);
    }

    private class NoteItemOnClickListener implements View.OnClickListener {
        private Note note;
        private NotesListFragment parent;

        public NoteItemOnClickListener(Note note, NotesListFragment parent) {
            this.note = note;
            this.parent = parent;
        }

        @Override
        public void onClick(View view) {
            parent.showNote(note);
        }
    }
}
