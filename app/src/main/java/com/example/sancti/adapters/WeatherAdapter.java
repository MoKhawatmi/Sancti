package com.example.sancti.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sancti.R;
import com.example.sancti.classes.Weather;


import java.util.ArrayList;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.MyViewHolder> {

    Context context;
    ArrayList<Weather> items;

    public WeatherAdapter(Context context, ArrayList<Weather> items){
        this.context=context;
        this.items=items;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView date,minTemp,maxTemp,condition;
        ImageView icon;

        public MyViewHolder(@NonNull View v) {
            super(v);

            date=v.findViewById(R.id.weatherDate);
            minTemp=v.findViewById(R.id.weatherMin);
            maxTemp=v.findViewById(R.id.weatherMax);
            condition=v.findViewById(R.id.weatherCondition);
            icon=v.findViewById(R.id.weatherIcon);
        }
    }

    @Override
    public WeatherAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.weather_layout, parent, false);

        return new WeatherAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherAdapter.MyViewHolder holder, int position) {

        holder.date.setText(context.getResources().getString(R.string.day)+items.get(position).getDate());
        holder.minTemp.setText(context.getResources().getString(R.string.min)+items.get(position).getMin());
        holder.maxTemp.setText(context.getResources().getString(R.string.max)+items.get(position).getMax());
        holder.condition.setText(items.get(position).getCondition());
        try {
            Glide.with(context).load(items.get(position).getImage()).into(holder.icon);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
