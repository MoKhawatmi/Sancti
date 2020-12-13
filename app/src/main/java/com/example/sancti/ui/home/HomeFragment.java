package com.example.sancti.ui.home;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amadeus.resources.FlightOfferSearch;
import com.amadeus.resources.HotelOffer;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.sancti.FlightsActivity;
import com.example.sancti.ImageActivity;
import com.example.sancti.R;
import com.example.sancti.adapters.RecycleAdapter;
import com.example.sancti.adapters.WeatherAdapter;
import com.example.sancti.classes.Async;
import com.example.sancti.classes.AsyncWeather;
import com.example.sancti.classes.CovidAsync;
import com.example.sancti.classes.Hotel;
import com.example.sancti.classes.PhotosAsync;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import com.example.sancti.classes.Weather;
import com.example.sancti.interfaces.AsyncResponse;
import com.example.sancti.interfaces.CovidResponse;
import com.example.sancti.interfaces.WeatherResponse;
import com.example.sancti.interfaces.photosResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class HomeFragment extends Fragment {


    public AutoCompleteTextView searchBar;
    public TextView welcomeText;
    public Button searchButton;
    public Button hotelsButton;
    public Button flightsButton;
    public LinearLayout optionsLayout;
    public PopupWindow popWindow;
    public int mDeviceHeight;
    public RecyclerView recyclerView;
    public TextView covidText;
    public TextView covidConfirmed;
    public TextView covidDead;
    public TextView covidRecovered;
    public TextView weatherState;
    public RecyclerView weatherRecycler;

    boolean flagAnim;
    LinearLayout layout;
    AsyncResponse response;

    public TextView hotelHeader;

    String cityCode;
    String inputCountry;
    HotelOffer[] hotelOffers;

    SQLiteDatabase tourismBase;

    WeatherResponse WResponse;

    photosResponse pResponse;

    CovidResponse covidResponse;

    GridLayout photoGrid;

    String today;

    TextView textViewNameCountry;
    String[] photoArray;

    RelativeLayout covidLayout;
    ImageView welcomeImage;

    LinearLayout topLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        tourismBase = getActivity().openOrCreateDatabase("data", getActivity().MODE_PRIVATE, null);
        tourismBase.execSQL("CREATE TABLE IF NOT EXISTS favorites(id integer primary key AUTOINCREMENT, obj text, type int);");


        response = new AsyncResponse() {

            @Override
            public void processFinish(HotelOffer[] output) {
                Log.d("tag", "process hotels finish");
                try {
                    hotelOffers = output;
                    ArrayList<Hotel> hotels = new ArrayList<>();
                    for (HotelOffer offer : hotelOffers) {
                        hotels.add(new Hotel(offer.getHotel().getHotelId(), offer.getHotel().getName(), offer.getHotel().getAddress().getCityName() + " - " + offer.getHotel().getAddress().getLines()[0], Double.valueOf(offer.getOffers()[0].getPrice().getTotal()) + offer.getOffers()[0].getPrice().getCurrency()));
                        Log.d("hotelId in frag", offer.getHotel().getHotelId());
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

        WResponse = new WeatherResponse() {
            @Override
            public void processFinish(ArrayList<Weather> output) {
                Log.d("Weather", "weather response working");

                try {
                    ArrayList<Weather> weathers = output;
                    if (weathers.isEmpty()) {
                        weatherState.setVisibility(View.VISIBLE);
                        weatherState.setText(getResources().getString(R.string.no_weather));
                    } else {
                        weatherState.setVisibility(View.VISIBLE);
                        WeatherAdapter adapter = new WeatherAdapter(getContext(), weathers);
                        weatherRecycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
                        weatherRecycler.setAdapter(adapter);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        pResponse = new photosResponse() {
            @Override
            public void processFinish(final String[] photos) {
                photoArray = photos;
                try {
                    for (int i = 0; i < 6; i++) {
                        ImageView image = (ImageView) photoGrid.getChildAt(i);
                        Glide.with(getActivity()).load(photos[i]).into(image);
                        image.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getActivity(), ImageActivity.class);
                                intent.putExtra("choosenImage", photos[Integer.valueOf(view.getTag().toString())]);
                                startActivity(intent);
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        covidResponse = new CovidResponse() {
            @Override
            public void processFinish(String[] covidState) {
                covidText.setText(getResources().getString(R.string.asof) +" "+ today +" "+ getResources().getString(R.string.covid_state) +" "+ inputCountry +" "+ getResources().getString(R.string.is));
                covidConfirmed.setText(covidState[0] +" "+ getResources().getString(R.string.confirmed));
                covidDead.setText(covidState[1] +" "+ getResources().getString(R.string.dead));
                covidRecovered.setText(covidState[2] +" "+ getResources().getString(R.string.recovered));
                covidLayout.setVisibility(View.VISIBLE);
            }
        };


        flagAnim = true;
        welcomeText = view.findViewById(R.id.text_home);
        searchButton = view.findViewById(R.id.search_button);
        optionsLayout = view.findViewById(R.id.options_layout);
        hotelsButton = view.findViewById(R.id.hotels_button);
        flightsButton = view.findViewById(R.id.flights_button);
        searchBar = view.findViewById(R.id.search_bar);
        final float y = optionsLayout.getY();
        optionsLayout.setAlpha(0);
        optionsLayout.animate().translationYBy(150);
        layout = view.findViewById(R.id.LinearHome);
        covidText = view.findViewById(R.id.covidState);
        weatherState = view.findViewById(R.id.weatherState);
        weatherRecycler = view.findViewById(R.id.weatherRecycler);
        photoGrid = view.findViewById(R.id.photoGrid);
        textViewNameCountry = view.findViewById(R.id.countryName);
        covidRecovered = view.findViewById(R.id.covidRecovered);
        covidDead = view.findViewById(R.id.covidDead);
        covidConfirmed = view.findViewById(R.id.covidConfirmed);
        covidLayout = view.findViewById(R.id.covid_layout);
        welcomeImage = view.findViewById(R.id.welcome_image);
        topLayout = view.findViewById(R.id.topLayout);

        final ArrayAdapter autocompletetextAdapter = new ArrayAdapter<String>(
                getContext(),
                android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.countries));

        searchBar.setAdapter(autocompletetextAdapter);


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!searchBar.getText().toString().isEmpty()) {
                    try{
                    inputCountry = searchBar.getText().toString();
                    textViewNameCountry.setText(inputCountry);
                    hotelsButton.setEnabled(true);
                    flightsButton.setEnabled(true);
                    if (flagAnim) {
                        flagAnim = false;
                        optionsLayout.setVisibility(View.VISIBLE);
                        welcomeText.animate().alpha(0).setDuration(200).start();
                        welcomeImage.animate().alpha(0).setDuration(200).start();
                        welcomeText.animate().translationYBy(-1000).setDuration(500).start();
                        welcomeImage.animate().translationYBy(-1000).setDuration(500).start();
                        optionsLayout.animate().translationYBy(-150).alpha(1).setDuration(500).start();
                    }
                    InputMethodManager m = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    m.hideSoftInputFromWindow(searchBar.getWindowToken(), 0);
                    autocompletetextAdapter.getPosition(searchBar.getText());
                    String[] arr1 = getResources().getStringArray(R.array.countries).clone();
                    int index = Arrays.asList(arr1).indexOf(searchBar.getText().toString());
                    Log.d("tag", index + "");
                    String[] arr2 = getResources().getStringArray(R.array.iata).clone();
                    cityCode = Arrays.asList(arr2).get(index);


                    InputStream is = getResources().openRawResource(R.raw.countries_json);
                    Writer writer = new StringWriter();
                    char[] buffer = new char[1024];
                    try {
                        Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                        int n;
                        while ((n = reader.read(buffer)) != -1) {
                            writer.write(buffer, 0, n);
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    String jsonString = writer.toString();
                    try {
                        JSONArray arr = new JSONArray(jsonString);
                        JSONObject obj = arr.getJSONObject(index);
                        String link = obj.getString("link");
                        Log.d("image link", link);

                        Glide.with(getActivity()).load(link).into(new CustomTarget<Drawable>() {
                            @Override
                            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                                topLayout.setBackground(resource);
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {

                            }
                        });


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    Log.d("tag", cityCode);
                    callCovidState(inputCountry);
                    callPhotos(inputCountry);
                    callWeatherState(inputCountry);
                }catch (Exception e){
                        Toast.makeText(getActivity(), getResources().getString(R.string.error_occured), Toast.LENGTH_LONG).show();
                    }}
                else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.enter_country_name), Toast.LENGTH_LONG).show();
                    hotelsButton.setEnabled(false);
                    flightsButton.setEnabled(false);
                }
            }
        });

        hotelsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new Async(response).execute(cityCode, "1");

            }
        });

        flightsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FlightsActivity.class);
                intent.putExtra("code", cityCode);
                intent.putExtra("country", inputCountry);
                startActivity(intent);
            }
        });


        return view;
    }

    public void ShowHotelsPopup(ArrayList<Hotel> hotels) {
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View inflatedView;
        inflatedView = layoutInflater.inflate(R.layout.popup_layout, null, false);
        recyclerView = inflatedView.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        RecycleAdapter adapt;
        adapt = new RecycleAdapter(getContext(), hotels);
        recyclerView.setAdapter(adapt);
        hotelHeader = inflatedView.findViewById(R.id.hotelsHeader);
        if (hotels.size() == 0) {
            hotelHeader.setVisibility(View.VISIBLE);
            hotelHeader.setText(getResources().getString(R.string.no_hotels));
        }

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        final Point size = new Point();
        display.getSize(size);
        mDeviceHeight = size.y;

        // set height depends on the device size
        popWindow = new PopupWindow(inflatedView, size.x - 50, size.y - 100, true);
        // set a background drawable with rounders corners
        // make it focusable to show the keyboard to enter in `EditText`
        popWindow.setFocusable(true);
        // make it outside touchable to dismiss the popup window
        popWindow.setOutsideTouchable(true);

        popWindow.setAnimationStyle(R.style.PopupAnimation);
        // show the popup at bottom of the screen and set some margin at bottom ie,
        popWindow.showAtLocation(layout, Gravity.BOTTOM, 0, 100);

    }

    public void callCovidState(String country) {
        String param = country.trim().toLowerCase().replaceAll("\\s", "-");
        Log.d("covid country", param);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -2);
        String yesterday = dateFormat.format(cal.getTime());

        Calendar cal2 = Calendar.getInstance();
        cal2.add(Calendar.DATE, -1);
        today = dateFormat.format(cal2.getTime());

        if (country.equalsIgnoreCase("united states")) {
            try {
                new CovidAsync(covidResponse).execute("https://api.covidtracking.com/v1/us/current.json", "1");
            } catch (Exception e) {
                e.printStackTrace();
            }


        } else {
            String endpoint = "https://api.covid19api.com/country/" + param + "?from=" + yesterday + "T00:00:00Z&to=" + today + "T00:00:00Z";
            Log.d("covid endpoint", endpoint);
            String[] covidResult;
            try {
                new CovidAsync(covidResponse).execute(endpoint, "2");
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void callWeatherState(String country) {
        weatherState.setText(getResources().getString(R.string.current_weather) +" "+ country + ":");
        try {
            new AsyncWeather(WResponse).execute("http://api.weatherapi.com/v1/forecast.json?key=195cd50f4c8e410bab3211727202209&q=" + cityCode + "&days=5");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void callPhotos(String country) {
        String query = country.replaceAll("\\s", "-");
        try {
            new PhotosAsync(pResponse).execute("https://api.unsplash.com/search/photos/?page=1&per_page=6&content_filter=high&query=" + query + "&client_id=Sit0MY9EXj9S5-LyuEAiAlvCGy5emdVWGq9zMoGm-sk");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


