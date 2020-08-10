package com.example.sancti.ui.trips;

import android.app.Dialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.sancti.R;

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
    SQLiteDatabase tourismBase;
    public void addingTrip(){

    }

    protected void showDialog(){

        Dialog dialog = new Dialog(getActivity());
        dialog.setCancelable(true);

        View view  = getActivity().getLayoutInflater().inflate(R.layout.custom_dialog_layout, null);
        dialog.setContentView(view);

        EditText tripTitle = view.findViewById(R.id.tripTitle);
        


        dialog.show();
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_trips, container, false);
        tourismBase = getActivity().openOrCreateDatabase("data", getActivity().MODE_PRIVATE, null);


        addTrip=v.findViewById(R.id.addTripBtn);

        addTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addingTrip();
            }
        });

        return v;
    }



}