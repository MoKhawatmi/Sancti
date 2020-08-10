package com.example.sancti.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sancti.R;
import com.example.sancti.classes.Dish;

import java.util.ArrayList;

public class DishesAdapter extends RecyclerView.Adapter<DishesAdapter.MyViewHolder> {
    ArrayList<Dish> items;
    Context context;

    public DishesAdapter(Context context,ArrayList<Dish> items){
        this.items=items;
        this.context=context;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView title,description;
        public MyViewHolder(@NonNull View v) {
            super(v);
            title=v.findViewById(R.id.dishTitle);
            description=v.findViewById(R.id.dishDescription);
        }
    }

    @Override
    public DishesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dishes_layout, parent, false);

        return new DishesAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DishesAdapter.MyViewHolder holder, int position) {
        holder.title.setText(items.get(position).getTitle());
        holder.description.setText(items.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
