package com.example.sancti.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sancti.R;
import com.example.sancti.classes.Event;

import java.util.ArrayList;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.MyViewHolder> {

    ArrayList<Event> items;
    Context context;

    public EventsAdapter(Context context,ArrayList<Event> items){
        this.context=context;
        this.items=items;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView title,description,date;
        ImageView image;
        public MyViewHolder(@NonNull View v) {
            super(v);
            title=v.findViewById(R.id.eventTitle);
            date=v.findViewById(R.id.eventDate);
            description=v.findViewById(R.id.eventDescription);
            image=v.findViewById(R.id.eventImage);
        }
    }

    @Override
    public EventsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.events_layout, parent, false);

        return new EventsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(EventsAdapter.MyViewHolder holder, int position) {
        holder.title.setText(items.get(position).getTitle());
        holder.date.setText(items.get(position).getDate());
        holder.description.setText(items.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
