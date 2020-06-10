package com.example.notesapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private static final String TAG = "RecyclerViewAdapter";
    private int n_title = 22;
    private int n_entry = 50;

    private List<Note> noteList;
    private Context context;

    public RecyclerViewAdapter(List<Note> noteList, Context context) {
        this.noteList = noteList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called");
        String entryExcerpt;
        String titleExcerpt;

        String title = noteList.get(position).getTitle();
        titleExcerpt = title.substring(0, Math.min(title.length(), n_title));
        holder.title.setText(titleExcerpt);

        String entry = noteList.get(position).getEntry();
        entryExcerpt = entry.substring(0, Math.min(entry.length(), n_entry));
        holder.entry.setText(entryExcerpt);

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked on: " + noteList.get(position).getTitle());

                Intent intent = new Intent(context, UpdateNote.class);
                intent.putExtra("title", noteList.get(position).getTitle());
                intent.putExtra("entry", noteList.get(position).getEntry());
                intent.putExtra("id", position + 1);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        RelativeLayout relativeLayout;
        TextView title;
        TextView entry;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.list_title_text);
            entry = itemView.findViewById(R.id.list_entry_text);
            relativeLayout = itemView.findViewById(R.id.relative_layout);

        }

    }
}
