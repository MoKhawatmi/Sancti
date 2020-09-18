package com.example.sancti.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.example.sancti.R;
import com.example.sancti.TripActivity;
import com.example.sancti.classes.Trip;

import java.util.ArrayList;

public class TripsListAdapter extends RecyclerView.Adapter<TripsListAdapter.MyViewHolder> {

    Context context;
    ArrayList<Trip> items;

    public TripsListAdapter(Context context,ArrayList<Trip> items){
        this.items=items;
        this.context=context;
    }

     class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        public MyViewHolder(View v) {
            super(v);
            title=v.findViewById(R.id.tripTitleRecycler);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent=new Intent(context, TripActivity.class);
            intent.putExtra("TripTitle",items.get(getAdapterPosition()).getTitle());
            intent.putExtra("TripId",items.get(getAdapterPosition()).getId());
            context.startActivity(intent);
        }
    }

    @Override
    public TripsListAdapter.MyViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trips_list_recycler_layout, parent, false);

        return new TripsListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder( TripsListAdapter.MyViewHolder holder, int position) {
            holder.title.setText(items.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
