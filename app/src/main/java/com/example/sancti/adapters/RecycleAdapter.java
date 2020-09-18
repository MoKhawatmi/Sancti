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
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.HotelSentiment;
import com.example.sancti.R;
import com.example.sancti.classes.Hotel;
import com.example.sancti.classes.RatingAsync;
import com.example.sancti.interfaces.RatingResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.MyViewHolder> {

    ArrayList<Hotel> items;
    Context context;
    SQLiteDatabase toursimBase;

    public RecycleAdapter(Context context,ArrayList myDataset) {
        items=myDataset;
        this.context=context;
        toursimBase=context.openOrCreateDatabase("data", context.MODE_PRIVATE, null);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, RatingResponse {
        TextView name,location,price,timesRated,hotelRating;
        RelativeLayout ratingLayout;
        RelativeLayout parentHotelRelative;
        Button addHotelToFav;
        public MyViewHolder(View v) {
            super(v);
            name=v.findViewById(R.id.hotelName);
            location=v.findViewById(R.id.hotelLocation);
            price=v.findViewById(R.id.roomPrice);
            timesRated=v.findViewById(R.id.timesRated);
            hotelRating=v.findViewById(R.id.hotelRating);
            addHotelToFav=v.findViewById(R.id.addHotelToFav);
            parentHotelRelative=v.findViewById(R.id.parentHotelRelative);
            ratingLayout=v.findViewById(R.id.ratingLayout);
            parentHotelRelative.setOnClickListener(this);
            addHotelToFav.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            if(view.getId()==R.id.addHotelToFav){
                Gson g=new Gson();
                try{
                toursimBase.execSQL("insert into favorites (obj) values ('"+g.toJson(items.get(getAdapterPosition()),Hotel.class)+"') ;");
                Log.d("JSON",g.toJson(items.get(getAdapterPosition()),Hotel.class));

                }catch (Exception e){
                  e.printStackTrace();
                }

                Cursor resultSet = toursimBase.rawQuery("Select * from favorites",null);
                if (resultSet != null && resultSet.moveToFirst()){
                    do {
                        String json=resultSet.getString(1);
                        Log.d("JSON",json);
                        Gson gson = new Gson();
                        Hotel h=gson.fromJson(json,Hotel.class);
                        Log.d("Hotel",h.getName()+" "+h.getLocation()+"  "+h.getPrice());
                    } while (resultSet.moveToNext());
                }
            }
            else{
            if(ratingLayout.getVisibility()==View.VISIBLE){
                ratingLayout.setVisibility(View.GONE);
            }else{
               // RatingAsync task=new RatingAsync(this);
                // task.execute(items.get(getAdapterPosition()).getId());
            }
            }

        }

        @Override
        public void processFinish(HotelSentiment[] output) {
            timesRated.setText(output[0].getNumberOfReviews());
            hotelRating.setText(output[0].getOverallRating());
            ratingLayout.setVisibility(View.VISIBLE);
        }

    }

    @NonNull
    @Override
    public RecycleAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hotels, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleAdapter.MyViewHolder holder, int position) {
        holder.name.setText(items.get(position).getName());
        holder.location.setText(items.get(position).getLocation());
        holder.price.setText(items.get(position).getPrice());
    }


    @Override
    public int getItemCount() {
        return items.size();
    }
}
