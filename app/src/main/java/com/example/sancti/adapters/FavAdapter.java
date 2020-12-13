package com.example.sancti.adapters;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sancti.R;
import com.example.sancti.classes.Flight;
import com.example.sancti.classes.Hotel;

import java.util.ArrayList;

public class FavAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    ArrayList<Hotel> hotels;
    ArrayList<Flight> flights;
    int counter1=0;
    int counter2=0;
    int hszie;
    int fsize;
    SQLiteDatabase tourismBase;
    ArrayList<Object> arr;

    public FavAdapter(Context context, ArrayList<Hotel> hotels, ArrayList<Flight> flights){
        this.context=context;
        this.hotels=hotels;
        this.flights=flights;
        hszie=hotels.size();
        fsize=flights.size();
        this.tourismBase=context.openOrCreateDatabase("data", context.MODE_PRIVATE, null);
        arr=new ArrayList<>();
        arr.addAll(hotels);
        arr.addAll(flights);
    }

     class HotelViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name,location,price;
        Button remove;

        public HotelViewHolder(@NonNull View v) {
            super(v);
            name=v.findViewById(R.id.favHotelName);
            location=v.findViewById(R.id.favHotelLocation);
            price=v.findViewById(R.id.favRoomPrice);
            remove=v.findViewById(R.id.removeHotelFav);
            remove.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int id;
            if(getAdapterPosition()<hszie){
                id=((Hotel) arr.get(getAdapterPosition())).getDbId();
                try{
                    tourismBase.execSQL("Delete from favorites where id = "+id);
                }catch (Exception e){
                    e.printStackTrace();
                }
                hszie--;
                arr.remove(getAdapterPosition());
                notifyItemRemoved(getAdapterPosition());
                notifyDataSetChanged();
            }else{

            }
        }
    }

     class FlightViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name,depTime,airline,time,dest,price;
        Button remove;
        public FlightViewHolder(@NonNull View v) {
            super(v);
            name=v.findViewById(R.id.favFlightName);
            depTime=v.findViewById(R.id.favDepTime);
            airline=v.findViewById(R.id.favAirLine);
            time=v.findViewById(R.id.favFlightTime);
            dest=v.findViewById(R.id.favFlightDestination);
            price=v.findViewById(R.id.favFlightPrice);
            remove=v.findViewById(R.id.removeFlightFav);
            remove.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int id;
            if(getAdapterPosition()<hszie){

            }else{
                id=((Flight) arr.get(getAdapterPosition())).getDbId();
                try{
                    tourismBase.execSQL("Delete from favorites where id = "+id);
                }catch (Exception e){
                    e.printStackTrace();
                }
                fsize--;
                arr.remove(getAdapterPosition());
                notifyItemRemoved(getAdapterPosition());
                notifyDataSetChanged();
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position<hszie){
            return 1;
        }else{
            return 2;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==1){
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fav_hotel_layout, parent, false);
            return new HotelViewHolder(itemView);
        }else{
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fav_flight_layout, parent, false);
            return new FlightViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder.getItemViewType()==1){
            HotelViewHolder hoho=(HotelViewHolder) holder;
            hoho.name.setText(context.getResources().getString(R.string.hotel_name)+": "+((Hotel) arr.get(position)).getName());
            hoho.price.setText(context.getResources().getString(R.string.offer_price)+": "+((Hotel) arr.get(position)).getPrice());
            hoho.location.setText(context.getResources().getString(R.string.hotel_location)+": "+((Hotel) arr.get(position)).getLocation());

           // hoho.name.setText(hotels.get(counter1).getName());
           // hoho.price.setText(hotels.get(counter1).getPrice());
           // hoho.location.setText(hotels.get(counter1).getLocation());
           // counter1++;
        }else{
            FlightViewHolder flho=(FlightViewHolder) holder;

            flho.airline.setText(context.getResources().getString(R.string.airline_code)+": "+((Flight)arr.get(position)).getAirLine());
            flho.depTime.setText(context.getResources().getString(R.string.dep_time)+": "+((Flight)arr.get(position)).getDepTime());
            flho.name.setText(context.getResources().getString(R.string.airport_code)+": "+((Flight)arr.get(position)).getName());
            flho.time.setText(context.getResources().getString(R.string.flight_duration)+": "+((Flight)arr.get(position)).getTime());
            flho.price.setText(context.getResources().getString(R.string.flight_price)+": "+((Flight)arr.get(position)).getPrice());
            flho.dest.setText(context.getResources().getString(R.string.destination)+": "+((Flight)arr.get(position)).getDestination());

            /*
            flho.airline.setText("Airline code: "+flights.get(counter2).getAirLine());
            flho.depTime.setText("Departs at: "+flights.get(counter2).getDepTime());
            flho.name.setText("Airport code: "+flights.get(counter2).getName());
            flho.time.setText("Duration: "+flights.get(counter2).getTime());
            flho.price.setText("Price: "+flights.get(counter2).getPrice());
            flho.dest.setText("Destination: "+flights.get(counter2).getDestination());
            counter2++;
            */
        }
    }


    @Override
    public int getItemCount() {
        return arr.size();
    }
}
