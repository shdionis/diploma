package ru.yandex.sharov.example.notes;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Random;

public class NotesRecyclerViewAdapter extends RecyclerView.Adapter {

    private List<Note> dataList;

    public NotesRecyclerViewAdapter(List<Note> dataSource){
        dataList = dataSource;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_list_item, parent, false);
        NoteViewHolder nvh = new NoteViewHolder(view);
        return nvh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        View ll = ((NoteViewHolder)holder).getTitle();
        Note n = dataList.get(position);
        TextView tvTitle = ll.findViewById(R.id.tvTitle);
        TextView tvDate = ll.findViewById(R.id.tvDate);
        TextView tvText = ll.findViewById(R.id.tvText);
        View circlePoint = ll.findViewById(R.id.circlePoint);
        Random r = new Random();
        GradientDrawable gd = (GradientDrawable) circlePoint.getBackground();
        gd.setColor(Color.argb(255, 46, 46, 46));

        tvDate.setText(n.getDate());
        tvTitle.setText(n.getTite());
        tvText.setText(n.getShortText(40));
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
}
