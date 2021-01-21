package com.android.helloapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.helloapp.R;
import com.android.helloapp.entity.Note;
import com.android.helloapp.viewmodel.NoteViewModel;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter <NoteAdapter.NoteHolder>{

    private Context contexto;
    private List<Note> notas = new ArrayList<>();

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View item = LayoutInflater.from(parent.getContext())
                  .inflate(R.layout.note_item, parent , false);

      return new NoteHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        Note note = notas.get(position);
        holder.textViewTitle.setText(note.getTitle());
        holder.textViewDescription.setText(note.getDescription());
        holder.textViewPriority.setText(String.valueOf(note.getPriority()));

    }

    @Override
    public int getItemCount() {
      return  notas.size();
    }

    public void setNotas(List<Note> notas) {
        this.notas = notas;
        notifyDataSetChanged();
    }

    class NoteHolder extends  RecyclerView.ViewHolder{
        private TextView textViewTitle ;
        private TextView textViewDescription ;
        private TextView textViewPriority ;

        public NoteHolder(View itemView){
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            textViewPriority = itemView.findViewById(R.id.text_view_priority) ;
        }

    }
}
