package com.example.sancti.ui.home;

import android.content.Context;
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

import com.example.sancti.R;
import com.example.sancti.adapters.DishesAdapter;
import com.example.sancti.adapters.EventsAdapter;
import com.example.sancti.adapters.FlightAdapter;
import com.example.sancti.adapters.RecycleAdapter;
import com.example.sancti.adapters.TraditionsAdapter;
import com.example.sancti.classes.Dish;
import com.example.sancti.classes.Event;
import com.example.sancti.classes.Flight;
import com.example.sancti.classes.Hotel;
import com.example.sancti.classes.Tradition;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

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


    void setSimpleList(ListView listView){
    }

    // call this method when required to show popup
    public void onShowPopup(View v){

        LayoutInflater layoutInflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View inflatedView;
        // inflate the custom popup layout
        ArrayList arr=new ArrayList();
        switch (v.getId()){

            case R.id.hotels_button:
                inflatedView = layoutInflater.inflate(R.layout.popup_layout, null,false);
                recyclerView=inflatedView.findViewById(R.id.recycler);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
                RecycleAdapter adapt;
                arr.add(new Hotel("name","location",100));
                arr.add(new Hotel("name","location",100));
                arr.add(new Hotel("name","location",100));
                adapt=new RecycleAdapter(getContext(),arr);
                recyclerView.setAdapter(adapt);
                break;

            case R.id.flights_button:
                inflatedView = layoutInflater.inflate(R.layout.popup_layout, null,false);
                recyclerView=inflatedView.findViewById(R.id.recycler);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
                FlightAdapter flightAdapter;
                arr.add(new Flight("name","from to","12/12/2020 11:00PM",100));
                arr.add(new Flight("name","from to","12/12/2020 11:00PM",100));
                arr.add(new Flight("name","from to","12/12/2020 11:00PM",100));
                flightAdapter=new FlightAdapter(getContext(),arr);
                recyclerView.setAdapter(flightAdapter);
                break;

            default:
                inflatedView = layoutInflater.inflate(R.layout.popup_layout2, null,false);
                traditions=inflatedView.findViewById(R.id.traditions);
                events=inflatedView.findViewById(R.id.events);
                dishes=inflatedView.findViewById(R.id.dishes);
                traditionsRecycler =inflatedView.findViewById(R.id.traditionList);
                eventsRecycler =inflatedView.findViewById(R.id.eventList);
                dishesRecycler =inflatedView.findViewById(R.id.dishesList);

                ArrayList<Tradition> traditionsList=new ArrayList();
                traditionsList.add(new Tradition("Title","TEXTTEXTTEXTTEXTTEXTTEXTTEXTTEXTTEXTTEXT"));
                traditionsList.add(new Tradition("Title","TEXTTEXTTEXTTEXTTEXTTEXTTEXTTEXTTEXTTEXT"));
                traditionsList.add(new Tradition("Title","TEXTTEXTTEXTTEXTTEXTTEXTTEXTTEXTTEXTTEXT"));
                traditionsRecycler.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
                TraditionsAdapter adaptTradition=new TraditionsAdapter(getContext(),traditionsList);
                traditionsRecycler.setAdapter(adaptTradition);

                ArrayList<Event> eventList=new ArrayList<>();
                eventList.add(new Event("Title","TEXTTEXTTEXTTEXTTEXTTEXTTEXTTEXTTEXTTEXT","first of july each year"));
                eventList.add(new Event("Title","TEXTTEXTTEXTTEXTTEXTTEXTTEXTTEXTTEXTTEXT","first of july each year"));
                eventList.add(new Event("Title","TEXTTEXTTEXTTEXTTEXTTEXTTEXTTEXTTEXTTEXT","first of july each year"));
                eventsRecycler.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
                EventsAdapter adaptEvents=new EventsAdapter(getContext(),eventList);
                eventsRecycler.setAdapter(adaptEvents);

                ArrayList<Dish> dishList=new ArrayList<>();
                dishList.add(new Dish("title","TEXTTEXTTEXTTEXTTEXTTEXTTEXTTEXTTEXTTEXT"));
                dishList.add(new Dish("title","TEXTTEXTTEXTTEXTTEXTTEXTTEXTTEXTTEXTTEXT"));
                dishList.add(new Dish("title","TEXTTEXTTEXTTEXTTEXTTEXTTEXTTEXTTEXTTEXT"));
                dishesRecycler.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
                DishesAdapter adaptDishes=new DishesAdapter(getContext(),dishList);
                dishesRecycler.setAdapter(adaptDishes);

                traditions.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(flagT){
                            flagT=false;
                            traditionsRecycler.setVisibility(View.VISIBLE);
                            traditions.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_down, 0, 0, 0);
                        }else{
                            flagT=true;
                            traditionsRecycler.setVisibility(View.GONE);
                            traditions.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_right, 0, 0, 0);
                        }
                    }
                });

                events.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(flagE){
                            flagE=false;
                            eventsRecycler.setVisibility(View.VISIBLE);
                            events.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_down, 0, 0, 0);
                        }else{
                            flagE=true;
                            eventsRecycler.setVisibility(View.GONE);
                            events.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_right, 0, 0, 0);
                        }
                    }
                });

                dishes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(flagD){
                            flagD=false;
                            dishesRecycler.setVisibility(View.VISIBLE);
                            dishes.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_down, 0, 0, 0);
                        }else{
                            flagD=true;
                            dishesRecycler.setVisibility(View.GONE);
                            dishes.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_right, 0, 0, 0);
                        }
                    }
                });


        }
        // get device size
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
        popWindow.showAtLocation(v, Gravity.BOTTOM, 0,100);

    }

   //popWindow.setAnimationStyle(R.style.PopupAnimation);

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
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
            }
        });

        hotelsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onShowPopup(view);
            }
        });

        flightsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onShowPopup(view);
            }
        });

        moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onShowPopup(view);
            }
        });


        return view;
    }
}

//                welcomeText.setVisibility(View.GONE);
//                optionsLayout.setVisibility(View.VISIBLE);
//                Animation a= AnimationUtils.loadAnimation(getContext(),R.anim.slide_up);
//                Animation b= AnimationUtils.loadAnimation(getContext(),R.anim.slide_down);
//                welcomeText.startAnimation(a);
//                optionsLayout.startAnimation(b);