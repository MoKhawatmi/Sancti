package com.example.sancti.ui.trips;

import android.app.Dialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sancti.R;
import com.example.sancti.adapters.RecycleAdapter;
import com.example.sancti.adapters.TripsListAdapter;
import com.example.sancti.classes.Trip;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class TripsFragment extends Fragment {

    /*This fragment will allow the user to log his trips to the app
    start a trip => dialog/popup to insert trip name
    =>table for the trip created => on the inside the table will contain trip logs or events
    TripEvent class=>String name,String date,String description,String(path) media(image , video)
    => and so we will have a list of trips, inside each item in this list will be a list or a sequence of events in that trip
    - and those will be stored in database

    **so when the user clicks on an item in the trips list, a new fragment/pop up containing the trip logs will show up
    => inside of it, there will be a button to add a new TripEvent with name, date, description, image/video (optional)
    */

    Button addTrip;
    RecyclerView tripsRecycler;
    ArrayList<Trip> tripsList;
    TripsListAdapter tripsListAdapter;
    SQLiteDatabase tourismBase;

    public void addingTrip(Trip trip){
        tourismBase.execSQL("INSERT INTO trips" + "(tripName)" + "VALUES ('" + trip.getTitle() + "')"+";");
        tripsList.add(trip);
        tripsListAdapter.notifyItemInserted(tripsList.size()-1);
        tripsListAdapter.notifyDataSetChanged();
        tourismBase.execSQL("CREATE TABLE IF NOT EXISTS '"+trip.getTitle().replaceAll("\\s+","_")+"' (id integer primary key AUTOINCREMENT, logText VARCHAR, logImage VARCHAR, logDate VARCAHR);");
    }

    protected void showDialog(){
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.custom_dialog_layout, null);

        final EditText tripTitle = view.findViewById(R.id.tripTitle);
        Button add=view.findViewById(R.id.btn_add);
        Button cancel=view.findViewById(R.id.btn_cancel);



        AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(getContext());
        dialogbuilder.setView(view);
        final AlertDialog dialogDetails = dialogbuilder.create();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title=tripTitle.getText().toString();
                if(title.matches("[a-zA-Z]+[a-zA-Z0-9\\s_]*")){
                    addingTrip(new Trip(title));
                    dialogDetails.hide();
                }
                else{
                    Toast.makeText(getContext(), "title should not start with a number or include any symbols", Toast.LENGTH_SHORT).show();
                }


            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogDetails.cancel();
            }
        });


        dialogDetails.show();
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_trips, container, false);
        tourismBase = getActivity().openOrCreateDatabase("data", getActivity().MODE_PRIVATE, null);
        tourismBase.execSQL("CREATE TABLE IF NOT EXISTS trips(id integer primary key AUTOINCREMENT, tripName VARCHAR);");


        addTrip=v.findViewById(R.id.addTripBtn);
        tripsRecycler=v.findViewById(R.id.tripRecycler);
        tripsList=new ArrayList<Trip>();

        Cursor resultSet = tourismBase.rawQuery("Select * from trips",null);
        if (resultSet != null && resultSet.moveToFirst()){
            do {
                tripsList.add(new Trip(resultSet.getInt(0),resultSet.getString(1).replaceAll("_","\\s")));
                Log.d("trip",resultSet.getInt(0)+"  "+resultSet.getString(1));
            } while (resultSet.moveToNext());
        }

        tripsListAdapter=new TripsListAdapter(getContext(),tripsList);
        tripsRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        tripsRecycler.setAdapter(tripsListAdapter);


        addTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        return v;
    }



}