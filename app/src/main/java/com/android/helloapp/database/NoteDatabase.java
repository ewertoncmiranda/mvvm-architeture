package com.android.helloapp.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.android.helloapp.dao.NoteDAO;
import com.android.helloapp.entity.Note;

@Database(entities = {Note.class} ,version = 1)
public abstract class NoteDatabase extends RoomDatabase {
    private static NoteDatabase instance ;

    public abstract NoteDAO noteDAO() ;

    public static synchronized  NoteDatabase getInstance(Context context){
        if(instance == null){
         instance = Room.databaseBuilder(context.getApplicationContext() ,
                        NoteDatabase.class ,"note_database" )
                        .fallbackToDestructiveMigration()
                        .addCallback(roomCallBack)
                        .build();
        } return instance;
    }

    private static  RoomDatabase.Callback roomCallBack = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDBAsyncTask(instance).execute();
        }
    };

    private static class PopulateDBAsyncTask extends AsyncTask<Void ,Void ,Void>{

        private NoteDAO dao ;

        private PopulateDBAsyncTask(NoteDatabase db){
            dao = db.noteDAO();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            dao.insert(new Note("Title 01","Descricao",1));
            dao.insert(new Note("Title 01","Descricao",2));
            dao.insert(new Note("Title 01","Descricao",4));
            dao.insert(new Note("Title 01","Descricao",3));
            dao.insert(new Note("Title 01","Descricao",2));
            dao.insert(new Note("Title 01","Descricao",1));
            return null;
        }
    }
}
