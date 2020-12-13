package com.example.sancti.ui.favorites;

import androidx.lifecycle.ViewModelProviders;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sancti.R;
import com.example.sancti.adapters.FavAdapter;
import com.example.sancti.classes.Flight;
import com.example.sancti.classes.Hotel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class Favorites extends Fragment {

    RecyclerView favRecycler;
    ArrayList<Hotel> favHotels;
    ArrayList<Flight> favFlights;
    SQLiteDatabase toursimBase;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.favorites_fragment, container, false);

        toursimBase=getActivity().openOrCreateDatabase("data", getActivity().MODE_PRIVATE, null);
        favHotels=new ArrayList();
        favFlights=new ArrayList();


        Cursor resultSet = toursimBase.rawQuery("Select * from favorites",null);
        if (resultSet != null && resultSet.moveToFirst()){
            do {
                if(resultSet.getInt(2)==1){
                    String json=resultSet.getString(1);
                    int id=resultSet.getInt(0);
                    Log.d("JSON",json);
                    Gson gson = new Gson();
                    Type hotelListType = new TypeToken<ArrayList<Hotel>>(){}.getType();
                    ArrayList<Hotel> h = gson.fromJson(json, hotelListType );
                    Log.d("hotel",h.toString());
                    Log.d("Hotel",h.get(0).getName()+" "+h.get(0).getLocation()+"  "+h.get(0).getPrice());
                    favHotels.add(new Hotel(h.get(0).getId(),h.get(0).getName(),h.get(0).getLocation(),h.get(0).getPrice(),id));
                }else{
                    String json=resultSet.getString(1);
                    int id=resultSet.getInt(0);
                    Log.d("JSON",json);
                    Gson gson = new Gson();
                    Type flightListType = new TypeToken<ArrayList<Flight>>(){}.getType();
                    ArrayList<Flight> f = gson.fromJson(json, flightListType );
                    Log.d("Flight",f.toString());
                    Log.d("Flight",f.get(0).getName()+" "+f.get(0).getAirLine()+"  "+f.get(0).getPrice());
                    favFlights.add(new Flight(f.get(0).getName(),f.get(0).getDepTime(),f.get(0).getAirLine(),f.get(0).getDestination(),f.get(0).getTime(),f.get(0).getPrice(),id));
                }
            } while (resultSet.moveToNext());
        }



        favRecycler=v.findViewById(R.id.favoritesRecycler);
        favRecycler.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));

        FavAdapter adapter=new FavAdapter(getContext(),favHotels,favFlights);

        favRecycler.setAdapter(adapter);

        return v;
    }

}