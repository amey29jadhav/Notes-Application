package com.amey.notes;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amey.notes.Database.AddNotesTable;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

    List<AddNotesTable> notesTable;
    private final Context context;
    private AppSettings.Orientation orientation;
    private MainActivity mainActivity;

    public NotesAdapter(MainActivity mainActivity, Context context, List<AddNotesTable> notesTable, AppSettings.Orientation orientation){
        this.mainActivity = mainActivity;
        this.context = context;
        this.notesTable = notesTable;
        this.orientation = orientation;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if(this.orientation == AppSettings.Orientation.ListView) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_row, parent, false);
        }else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_row_grid, parent, false);
        }
        return new NotesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final AddNotesTable notes = notesTable.get(position);
        holder.monthname.setVisibility(View.GONE);
        holder.title.setText( notes.title);
        holder.monthname.setText(notes.monthname);
        holder.time.setText(notes.time);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,AddNotesActivity.class);
                intent.putExtra("id",notes._id);
                mainActivity.startActivityForResult(intent,1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notesTable.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView monthname, title, time;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            monthname = (TextView) itemView.findViewById(R.id.monthname);
            title = (TextView) itemView.findViewById(R.id.title);
            time = (TextView) itemView.findViewById(R.id.time);

        }
    }
}
