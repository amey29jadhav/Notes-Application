package com.amey.notes;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amey.notes.Database.AddNotesTable;

import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder>  implements Filterable {

    List<AddNotesTable> notesTable;
    private final Context context;
    private AppSettings.Orientation orientation;
    private MainActivity mainActivity;
    List<AddNotesTable> filternotestable;

    public NotesAdapter(MainActivity mainActivity, Context context, List<AddNotesTable> notesTable, AppSettings.Orientation orientation){
        this.mainActivity = mainActivity;
        this.context = context;
        this.notesTable = notesTable;
        this.orientation = orientation;
        this.filternotestable = notesTable;
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
        final AddNotesTable notes = filternotestable.get(position);
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
        if(filternotestable != null & filternotestable.size() > 0) {
            return filternotestable.size();
        }else{
            return 0;
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    filternotestable = notesTable;
                } else {
                    List<AddNotesTable> filteredList = new ArrayList<>();
                    for (AddNotesTable row : notesTable) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.title.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    filternotestable = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filternotestable;
                filterResults.count = filternotestable.size();
                return filterResults;
            }


            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filternotestable = (ArrayList<AddNotesTable>) filterResults.values;
                notifyDataSetChanged();
            }
        };

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
