package com.example.sancti.adapters;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sancti.R;
import com.example.sancti.classes.Flight;
import com.example.sancti.classes.Hotel;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class FlightAdapter extends RecyclerView.Adapter<FlightAdapter.MyViewHolder> {
    ArrayList<Flight> items;
    Context context;
    SQLiteDatabase toursimBase;

    public FlightAdapter(Context context, ArrayList<Flight> items){
        this.items=items;
        this.context=context;
        toursimBase=context.openOrCreateDatabase("data", context.MODE_PRIVATE, null);
    }

     class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name,destination,time,price,airLine;
        Button addFlightToFav;

        public MyViewHolder(@NonNull View v) {
            super(v);
            name=v.findViewById(R.id.flightName);
            destination=v.findViewById(R.id.flightDestination);
            time=v.findViewById(R.id.flightTime);
            price=v.findViewById(R.id.flightPrice);
            airLine=v.findViewById(R.id.airLine);
            addFlightToFav=v.findViewById(R.id.addFlightToFav);
            addFlightToFav.setOnClickListener(this);
        }


         @Override
         public void onClick(View view) {

         }
    }



    @Override
    public FlightAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.flights, parent, false);

        return new FlightAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FlightAdapter.MyViewHolder holder, int position) {
        holder.name.setText(items.get(position).getName());
        holder.destination.setText(items.get(position).getDestination());
        holder.time.setText(items.get(position).getTime());
        holder.price.setText(items.get(position).getPrice());
    }


    @Override
    public int getItemCount() {
        return items.size();
    }
}
