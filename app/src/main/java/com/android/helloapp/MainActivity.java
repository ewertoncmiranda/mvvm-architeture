package com.android.helloapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.android.helloapp.adapter.NoteAdapter;
import com.android.helloapp.entity.Note;
import com.android.helloapp.viewmodel.NoteViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private NoteViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);

        RecyclerView recicler = findViewById(R.id.recycler_view);
        recicler.setLayoutManager(new LinearLayoutManager(this));
        recicler.setHasFixedSize(true);

        final NoteAdapter adapter = new NoteAdapter();
        recicler.setAdapter(adapter);

         viewModel = new ViewModelProvider
                  (this,ViewModelProvider.AndroidViewModelFactory
                  .getInstance(this.getApplication()))
                  .get(NoteViewModel.class);

         viewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
             @Override
             public void onChanged(List<Note> notes) {
                 //Update RecyclerView
                 adapter.setNotas(notes);
                 Toast.makeText(MainActivity.this,"onChanged" ,Toast.LENGTH_SHORT).show();
             }
         });
    }
}