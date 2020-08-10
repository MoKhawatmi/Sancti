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
import com.example.sancti.classes.Hotel;

import java.util.ArrayList;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.MyViewHolder> {

    ArrayList<Hotel> items;
    Context context;

    public RecycleAdapter(Context context,ArrayList myDataset) {
        items=myDataset;
        this.context=context;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name,location,price;
        ImageView image;
        public MyViewHolder(View v) {
            super(v);
            name=v.findViewById(R.id.hotelName);
            location=v.findViewById(R.id.hotelLocation);
            price=v.findViewById(R.id.roomPrice);
            image=v.findViewById(R.id.hotelImg);

        }
    }

    @NonNull
    @Override
    public RecycleAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hotels, parent, false);

        return new MyViewHolder(itemView);    }

    @Override
    public void onBindViewHolder(@NonNull RecycleAdapter.MyViewHolder holder, int position) {
        holder.name.setText(items.get(position).getName());
        holder.location.setText(items.get(position).getLocation());
        holder.price.setText(items.get(position).getPrice()+" JOD");
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
