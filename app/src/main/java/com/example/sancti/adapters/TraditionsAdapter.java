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
import com.example.sancti.classes.Tradition;

import java.util.ArrayList;

public class TraditionsAdapter extends RecyclerView.Adapter<TraditionsAdapter.MyViewHolder> {

    public ArrayList<Tradition> items;
    public Context context;

    public TraditionsAdapter(Context context,ArrayList<Tradition> items){
        this.items=items;
        this.context=context;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView title,description;
        public MyViewHolder(@NonNull View v) {
            super(v);
            img=v.findViewById(R.id.traditionImage);
            title=v.findViewById(R.id.traditionTitle);
            description=v.findViewById(R.id.traditionDescription);
        }
    }

    @Override
    public TraditionsAdapter.MyViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.traditions_layout, parent, false);

        return new TraditionsAdapter.MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(TraditionsAdapter.MyViewHolder holder, int position) {
        holder.title.setText(items.get(position).getTitle());
        holder.description.setText(items.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
