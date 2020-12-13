package com.example.sancti.adapters;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sancti.R;
import com.example.sancti.classes.Flight;
import com.example.sancti.classes.Hotel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
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
        TextView name,depTime,destination,time,price,airLine;
        Button addFlightToFav;

        public MyViewHolder(@NonNull View v) {
            super(v);
            name=v.findViewById(R.id.flightName);
            depTime=v.findViewById(R.id.depTime);
            destination=v.findViewById(R.id.flightDestination);
            time=v.findViewById(R.id.flightTime);
            price=v.findViewById(R.id.flightPrice);
            airLine=v.findViewById(R.id.airLine);
            addFlightToFav=v.findViewById(R.id.addFlightToFav);
            addFlightToFav.setOnClickListener(this);
        }


         @Override
         public void onClick(View view) {
            if(view.getId()==R.id.addFlightToFav){
                Gson g=new Gson();
                try{
                    toursimBase.execSQL("insert into favorites (obj,type) values ('["+g.toJson(items.get(getAdapterPosition()),Flight.class).toString()+"]',2) ;");
                    Log.d("JSON",g.toJson(items.get(getAdapterPosition()),Flight.class));
                    Toast.makeText(context, context.getResources().getString(R.string.added_to_fav), Toast.LENGTH_LONG).show();
                }catch (Exception e){
                    e.printStackTrace();
                }

                Cursor resultSet = toursimBase.rawQuery("Select * from favorites",null);
                if (resultSet != null && resultSet.moveToFirst()){
                    do {
                        String json=resultSet.getString(1);
                        Log.d("JSON",json);
                        Gson gson = new Gson();
                        Type flightListType = new TypeToken<ArrayList<Flight>>(){}.getType();
                        ArrayList<Flight> f = gson.fromJson(json, flightListType );
                        Log.d("Flight",f.toString());
                        Log.d("Flight",f.get(0).getName()+" "+f.get(0).getAirLine()+"  "+f.get(0).getPrice());
                    } while (resultSet.moveToNext());
                }
            }
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
        holder.name.setText(context.getResources().getString(R.string.airport_code)+": "+items.get(position).getName());
        holder.depTime.setText(context.getResources().getString(R.string.dep_time)+": "+items.get(position).getDepTime());
        holder.airLine.setText(context.getResources().getString(R.string.airline_code)+": "+items.get(position).getAirLine());
        holder.destination.setText(context.getResources().getString(R.string.destination)+": "+items.get(position).getDestination());
        holder.time.setText(context.getResources().getString(R.string.flight_duration)+": "+items.get(position).getTime());
        holder.price.setText(context.getResources().getString(R.string.flight_price)+": "+items.get(position).getPrice());
    }


    @Override
    public int getItemCount() {
        return items.size();
    }
}
