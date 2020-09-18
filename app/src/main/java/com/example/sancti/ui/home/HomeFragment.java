package com.example.sancti.ui.home;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amadeus.resources.FlightOfferSearch;
import com.amadeus.resources.HotelOffer;
import com.example.sancti.FlightsActivity;
import com.example.sancti.R;
import com.example.sancti.adapters.DishesAdapter;
import com.example.sancti.adapters.EventsAdapter;
import com.example.sancti.adapters.FlightAdapter;
import com.example.sancti.adapters.RecycleAdapter;
import com.example.sancti.adapters.TraditionsAdapter;
import com.example.sancti.classes.Async;
import com.example.sancti.classes.Dish;
import com.example.sancti.classes.Event;
import com.example.sancti.classes.Flight;
import com.example.sancti.classes.Hotel;
import com.example.sancti.classes.Tradition;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import com.amadeus.Amadeus;
import com.amadeus.Params;

import com.amadeus.exceptions.ResponseException;
import com.amadeus.referenceData.Locations;
import com.amadeus.resources.Location;
import com.example.sancti.interfaces.AsyncResponse;
import com.example.sancti.ui.attraction.Attraction;


public class HomeFragment extends Fragment {


    public AutoCompleteTextView searchBar;
    public TextView welcomeText;
    public Button searchButton;
    public Button hotelsButton;
    public Button flightsButton;
    public Button moreButton;
    public LinearLayout optionsLayout;
    public PopupWindow popWindow;
    public int mDeviceHeight;
    public RecyclerView recyclerView;


    public LinearLayout listLayout;
    public TextView traditions;
    public TextView events;
    public TextView dishes;
    public RecyclerView traditionsRecycler;
    public RecyclerView eventsRecycler;
    public RecyclerView dishesRecycler;
    boolean flagT=true;
    boolean flagE=true;
    boolean flagD=true;
    boolean flagAnim;
    LinearLayout layout;
    AsyncResponse response;
    Async task;

    String cityCode;
    HotelOffer[] hotelOffers;

    SQLiteDatabase tourismBase;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        tourismBase = getActivity().openOrCreateDatabase("data", getActivity().MODE_PRIVATE, null);
        tourismBase.execSQL("CREATE TABLE IF NOT EXISTS favorites(id integer primary key AUTOINCREMENT, obj text);");


        response = new AsyncResponse() {

            @Override
            public void processFinish(HotelOffer[] output) {
                    Log.d("tag", "process hotels finish");
                    try {
                        hotelOffers = output;
                        ArrayList<Hotel> hotels = new ArrayList<>();
                        for (HotelOffer offer : hotelOffers) {
                            hotels.add(new Hotel(offer.getHotel().getHotelId(),offer.getHotel().getName(),offer.getHotel().getAddress().getCityName()+" - "+offer.getHotel().getAddress().getLines()[0], Double.valueOf(offer.getOffers()[0].getPrice().getTotal())+offer.getOffers()[0].getPrice().getCurrency()));
                            Log.d("hotelId in frag",offer.getHotel().getHotelId());
                        }
                        ShowHotelsPopup(hotels);
                    } catch (Exception e) {
                        Log.d("tag", e.getMessage());
                    }
            }

            @Override
            public void processFinish(FlightOfferSearch[] output) {
            }
        };

        flagAnim=true;
        welcomeText=view.findViewById(R.id.text_home);
        searchButton=view.findViewById(R.id.search_button);
        optionsLayout=view.findViewById(R.id.options_layout);
        hotelsButton=view.findViewById(R.id.hotels_button);
        flightsButton=view.findViewById(R.id.flights_button);
        moreButton=view.findViewById(R.id.activities_button);
        searchBar=view.findViewById(R.id.search_bar);
        final float y=optionsLayout.getY();
        optionsLayout.setAlpha(0);
        optionsLayout.animate().translationYBy(150);
        layout=view.findViewById(R.id.LinearHome);

        final ArrayAdapter autocompletetextAdapter = new ArrayAdapter<String>(
                getContext(),
                android.R.layout.simple_dropdown_item_1line,getResources().getStringArray(R.array.countries));

        searchBar.setAdapter(autocompletetextAdapter);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flagAnim){
                flagAnim=false;
                welcomeText.animate().alpha(0).setDuration(200).start();
                welcomeText.animate().translationYBy(-1000).setDuration(500).start();
                optionsLayout.animate().translationYBy(-150).alpha(1).setDuration(500).start();
            }
                InputMethodManager m=(InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                m.hideSoftInputFromWindow(searchBar.getWindowToken(),0);
                autocompletetextAdapter.getPosition(searchBar.getText());
                String[] arr1=getResources().getStringArray(R.array.countries).clone();
                int index=Arrays.asList(arr1).indexOf(searchBar.getText().toString());
                Log.d("tag",index+"");
                String[] arr2=getResources().getStringArray(R.array.iata).clone();
                cityCode=Arrays.asList(arr2).get(index);
                Log.d("tag",cityCode);

            }
        });

        hotelsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new Async(response).execute(cityCode,"1");

            }
        });

        flightsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), FlightsActivity.class);
                intent.putExtra("code",cityCode);
                intent.putExtra("country",searchBar.getText().toString());
                startActivity(intent);
            }
        });

        moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return view;
    }

    public void ShowHotelsPopup(ArrayList<Hotel> hotels){
        LayoutInflater layoutInflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View inflatedView;
        inflatedView = layoutInflater.inflate(R.layout.popup_layout, null,false);
        recyclerView=inflatedView.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        RecycleAdapter adapt;
        adapt=new RecycleAdapter(getContext(),hotels);
        recyclerView.setAdapter(adapt);

        Display display = getActivity().getWindowManager().getDefaultDisplay();
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


