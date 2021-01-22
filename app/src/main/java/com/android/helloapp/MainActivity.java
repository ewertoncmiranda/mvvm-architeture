package com.android.helloapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.helloapp.activity.AddEditNoteActivity;
import com.android.helloapp.adapter.NoteAdapter;
import com.android.helloapp.entity.Note;
import com.android.helloapp.viewmodel.NoteViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private NoteViewModel viewModel;
    public static final Integer ADD_NOTE_REQUEST = 1 ;
    public static final Integer EDIT_NOTE_REQUEST = 2 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);

        FloatingActionButton buttonAdd = findViewById(R.id.button_add_note);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class);
                startActivityForResult(intent ,ADD_NOTE_REQUEST);
            }
        });

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

         new ItemTouchHelper(new ItemTouchHelper.SimpleCallback
                 (0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
             @Override
             public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                 return false;
             }

             @Override
             public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                viewModel.delete(adapter.getNoteAt(viewHolder.getAdapterPosition()));
                 Toast.makeText(MainActivity.this, "Note Delete", Toast.LENGTH_SHORT).show();
             }
         }).attachToRecyclerView(recicler);

         adapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
             @Override
             public void onItemClick(Note note) {
                 Intent intent = new Intent(MainActivity.this , AddEditNoteActivity.class);
                 intent.putExtra(AddEditNoteActivity.EXTRA_ID ,note.getId());
                 intent.putExtra(AddEditNoteActivity.EXTRA_TITLE ,note.getTitle());
                 intent.putExtra(AddEditNoteActivity.EXTRA_DESCRIPTION ,note.getDescription());
                 intent.putExtra(AddEditNoteActivity.EXTRA_PRIORITY ,note.getPriority());
                 startActivityForResult(intent ,EDIT_NOTE_REQUEST);
             }
         });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ADD_NOTE_REQUEST && resultCode ==  RESULT_OK){
            String title = data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION);
            Integer priority = data.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY,1);

            Note note =  new Note(title ,description ,priority);
            viewModel.insert(note);
            Toast.makeText(this, "Note Saved!", Toast.LENGTH_SHORT).show();
        }else if(requestCode == EDIT_NOTE_REQUEST && resultCode ==  RESULT_OK){
            int id = data.getIntExtra(AddEditNoteActivity.EXTRA_ID,-1);

            if(id == -1){
                 Toast.makeText(this, "CANT UPDATE", Toast.LENGTH_SHORT).show();
                 return;
             }
            String title = data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION);
            Integer priority = data.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY,1);

            Note note =  new Note(title ,description ,priority);
            note.setId(id);
            viewModel.update(note);
            Toast.makeText(this, "Note UPDATED!", Toast.LENGTH_SHORT).show();


        }else {
            Toast.makeText(this, "Note DONT Saved!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu ,menu);
        return true ;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_all:
                viewModel.deleteAll();;
                Toast.makeText(this,"All notes Deleted!" ,Toast.LENGTH_SHORT).show();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}