package ru.yandex.sharov.example.notes;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NotesActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        List<Note> data = new ArrayList<>();
        for (int i = 1; i <= 50; i++) {
            StringBuilder textNote = new StringBuilder();
            for (int j = 0; j < 30; j++) {
                textNote.append("Note").append(i).append(j);
            }

            data.add(new Note("Note"+i, (i%30)+"-"+i%12, textNote.toString()));
        }
        adapter = new NotesRecyclerViewAdapter(data);
        recyclerView.setAdapter(adapter);


    }
}
