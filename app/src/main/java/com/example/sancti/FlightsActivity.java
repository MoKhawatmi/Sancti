package com.example.sancti;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.amadeus.resources.FlightOfferSearch;
import com.amadeus.resources.HotelOffer;
import com.example.sancti.adapters.FlightAdapter;
import com.example.sancti.classes.Async;
import com.example.sancti.classes.Flight;
import com.example.sancti.classes.Hotel;
import com.example.sancti.interfaces.AsyncResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class FlightsActivity extends AppCompatActivity {

    AsyncResponse response;
    FlightOfferSearch[] flightOffers;
    public String cityCode;
    public RecyclerView recyclerView;
    public TextView flightsHeader;
    public PopupWindow popWindow;
    public int mDeviceHeight;
    LinearLayout layout;
    String destCountry;

    AutoCompleteTextView fromCountry;
    EditText depDate;
    EditText returnDate;
    EditText adultNumber;
    DatePickerDialog picker;

    public void search(View view){
        String[] arr1=getResources().getStringArray(R.array.countries).clone();
        int index= Arrays.asList(arr1).indexOf(fromCountry.getText().toString());
        String[] arr2=getResources().getStringArray(R.array.iata).clone();

        String from=Arrays.asList(arr2).get(index);
        String depart=depDate.getText().toString();
        String reDate;
        if(returnDate.getText().toString().isEmpty()){
            reDate="non";
        }else{
            reDate=returnDate.getText().toString();
        }
        int adults=Integer.valueOf(adultNumber.getText().toString());

        Log.d("values",from+" "+depart+" "+reDate+" "+adults);

        new Async(response).execute(cityCode,"2",from,depart,reDate,adults+"");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flights);
        layout=findViewById(R.id.LinearSearch);
        fromCountry=findViewById(R.id.fromCountry);
        depDate=findViewById(R.id.depDate);
        returnDate=findViewById(R.id.returnDate);
        adultNumber=findViewById(R.id.adultNumber);

        ArrayAdapter arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line,getResources().getStringArray(R.array.countries));
        fromCountry.setAdapter(arrayAdapter);

        Intent i=getIntent();
        cityCode=i.getStringExtra("code");
        destCountry=i.getStringExtra("country");
        Log.d("city code",cityCode);
        Log.d("country",destCountry);

        depDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    final Calendar cldr = Calendar.getInstance();
                    int day = cldr.get(Calendar.DAY_OF_MONTH);
                    int month = cldr.get(Calendar.MONTH);
                    int year = cldr.get(Calendar.YEAR);
                    // date picker dialog
                    picker = new DatePickerDialog(FlightsActivity.this,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    depDate.setText(year+ "-" + (Integer.valueOf(monthOfYear + 1)<10?"0"+(monthOfYear+1):monthOfYear+1)+ "-" + (Integer.valueOf(dayOfMonth)<10?"0"+dayOfMonth:dayOfMonth));
                                    picker.dismiss();
                                }
                            }, year, month, day);
                    picker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                    picker.show();
                }
            }
        });

        depDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(FlightsActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                depDate.setText(year+ "-" + (Integer.valueOf(monthOfYear + 1)<10?"0"+(monthOfYear+1):monthOfYear+1)+ "-" + (Integer.valueOf(dayOfMonth)<10?"0"+dayOfMonth:dayOfMonth));
                                picker.dismiss();
                            }
                        }, year, month, day);
                picker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                picker.show();
            }
        });

        returnDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    final Calendar cldr = Calendar.getInstance();
                    int day = cldr.get(Calendar.DAY_OF_MONTH);
                    int month = cldr.get(Calendar.MONTH);
                    int year = cldr.get(Calendar.YEAR);
                    // date picker dialog
                    picker = new DatePickerDialog(FlightsActivity.this,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    returnDate.setText(year+ "-" + (Integer.valueOf(monthOfYear + 1)<10?"0"+(monthOfYear+1):monthOfYear+1)+ "-" + (Integer.valueOf(dayOfMonth)<10?"0"+dayOfMonth:dayOfMonth));
                                    picker.dismiss();
                                }
                            }, year, month, day);
                    picker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                    picker.show();
                }
            }
        });

        returnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(FlightsActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                returnDate.setText(year+ "-" + (Integer.valueOf(monthOfYear + 1)<10?"0"+(monthOfYear+1):monthOfYear+1)+ "-" + (Integer.valueOf(dayOfMonth)<10?"0"+dayOfMonth:dayOfMonth));
                                picker.dismiss();
                            }
                        }, year, month, day);
                picker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                picker.show();
            }
        });

        response = new AsyncResponse() {

            @Override
            public void processFinish(HotelOffer[] output) {
                Log.d("tag", "process hotels finish");
            }

            @Override
            public void processFinish(FlightOfferSearch[] output) {
                Log.d("tag","process flights finish");
                try {
                    flightOffers=output;
                    ArrayList<Flight> flights=new ArrayList<>();
                    for(FlightOfferSearch offer:flightOffers){
                        flights.add(new Flight(offer.getSource(),offer.getItineraries()[0].getSegments()[0].getCarrierCode(),destCountry,offer.getItineraries()[0].getDuration(),offer.getPrice().getTotal()+" "+offer.getPrice().getCurrency()));
                    }
                    ShowFlightsPopup(flights);
                }catch (Exception e){
                    Log.d("tag",e.getMessage());
                }
            }
        };
    }

    public void ShowFlightsPopup(ArrayList<Flight> flights){
        LayoutInflater layoutInflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View inflatedView;
        inflatedView = layoutInflater.inflate(R.layout.popup_layout3, null,false);
        recyclerView=inflatedView.findViewById(R.id.recycler);
        flightsHeader=inflatedView.findViewById(R.id.flightsHeader);
        flightsHeader.setText("Flight from "+fromCountry.getText().toString()+" to "+destCountry+" on "+depDate.getText().toString());

        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        FlightAdapter adapt;
        adapt=new FlightAdapter(this,flights);
        recyclerView.setAdapter(adapt);

        Display display = this.getWindowManager().getDefaultDisplay();
        final Point size = new Point();
        display.getSize(size);
        mDeviceHeight = size.y;

        // set height depends on the device size
        popWindow = new PopupWindow(inflatedView, size.x - 50,size.y-100, true );
        // set a background drawable with rounders corners
        // make it focusable to show the keyboard to enter in `EditText`
        popWindow.setFocusable(true);
        // make it outside touchable to dismiss the popup window
        popWindow.setOutsideTouchable(true);

        popWindow.setAnimationStyle(R.style.PopupAnimation);
        // show the popup at bottom of the screen and set some margin at bottom ie,
        popWindow.showAtLocation(layout, Gravity.BOTTOM, 0,100);
    }
}