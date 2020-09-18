package com.example.sancti.classes;

import android.os.AsyncTask;
import android.util.Log;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.resources.FlightOfferSearch;
import com.amadeus.resources.HotelOffer;
import com.amadeus.shopping.FlightOffers;
import com.example.sancti.interfaces.AsyncResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Async extends AsyncTask<String,Void,String> {

    public AsyncResponse response = null;
    HotelOffer[] hotels;
    FlightOfferSearch[] flights;

    public Async(AsyncResponse response){
        this.response=response;
    }

    @Override
    protected String doInBackground(String... values) {

        try{
            Amadeus amadeus = Amadeus
                    .builder("NvRBVcE8owKujtAeJdqYawFisEWujOZP", "f4tG8oUl6znvuJlx")
                    .build();

            if(values[1]=="1"){
                HotelOffer[] offers = amadeus.shopping.hotelOffers.get(Params
                        .with("cityCode", values[0]));
                         hotels=offers;
            }else if(values[1]=="2"){
                if(values[4]=="non"){
                    FlightOfferSearch[] flightOffersSearches = amadeus.shopping.flightOffersSearch.get(
                            Params.with("originLocationCode", values[2])
                                    .and("destinationLocationCode", values[0])
                                    .and("departureDate", values[3])
                                    .and("adults", Integer.valueOf(values[5]))
                                    .and("max", 3));
                    flights=flightOffersSearches;
                }else{
                    FlightOfferSearch[] flightOffersSearches = amadeus.shopping.flightOffersSearch.get(
                            Params.with("originLocationCode", values[2])
                                    .and("destinationLocationCode", values[0])
                                    .and("departureDate", values[3])
                                    .and("returnDate", values[4]).and("adults", Integer.valueOf(values[5]))
                                    .and("max", 3));
                    flights=flightOffersSearches;
                }

            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        Log.d("vararg",values[1]);
        return values[1];
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.d("Tag",s);
        try {
            if(s=="1") {
                response.processFinish(hotels);
            }else{
                response.processFinish(flights);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
