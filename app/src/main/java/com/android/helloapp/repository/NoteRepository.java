package com.android.helloapp.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;

import com.android.helloapp.dao.NoteDAO;
import com.android.helloapp.database.NoteDatabase;
import com.android.helloapp.entity.Note;

import java.util.List;

public class NoteRepository {
    private NoteDAO noteDAO;
    private LiveData<List<Note>> allNotes ;

    public NoteRepository (Application app){
        NoteDatabase db = NoteDatabase.getInstance(app);
        noteDAO = db.noteDAO();
        allNotes = noteDAO.getAllNotes();
    }

    public void insert(Note note){
       new InsertNoteAsyncTask(noteDAO).execute(note);
    }

    public void update(Note note){
        new UpdateNoteAsyncTask(noteDAO).execute(note);
    }

    public void delete(Note note){
        new DeleteNoteAsyncTask(noteDAO).execute(note);
    }

    public void deleteAllNotes(){
        new DeleteAllNoteAsyncTask(noteDAO).execute();
    }

    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }

    private static class DeleteNoteAsyncTask extends AsyncTask <Note ,Void,Void>{

        private NoteDAO dao ;

        private DeleteNoteAsyncTask(NoteDAO dao){
            this.dao = dao ;
        }
        @Override
        protected Void doInBackground(Note... notes) {
            dao.delete(notes[0]);
            return null;
        }
    }

    private static class UpdateNoteAsyncTask extends AsyncTask <Note ,Void,Void>{

        private NoteDAO dao ;

        private UpdateNoteAsyncTask(NoteDAO dao){
            this.dao = dao ;
        }
        @Override
        protected Void doInBackground(Note... notes) {
            dao.update(notes[0]);
            return null;
        }
    }

    private static class InsertNoteAsyncTask extends AsyncTask <Note ,Void,Void>{

        private NoteDAO dao ;

        private InsertNoteAsyncTask(NoteDAO dao){
            this.dao = dao ;
        }
        @Override
        protected Void doInBackground(Note... notes) {
            dao.insert(notes[0]);
            return null;
        }
    }

    private static class DeleteAllNoteAsyncTask extends AsyncTask <Note ,Void,Void>{

        private NoteDAO dao ;

        private DeleteAllNoteAsyncTask(NoteDAO dao){
            this.dao = dao ;
        }
        @Override
        protected Void doInBackground(Note... notes) {
            dao.deleteAllNotes();
            return null;
        }
    }
}

