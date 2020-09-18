package com.example.sancti.classes;

import android.os.AsyncTask;
import android.util.Log;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.HotelSentiment;
import com.example.sancti.interfaces.RatingResponse;

public class RatingAsync extends AsyncTask<String,Void,String> {
    public RatingResponse response = null;
    HotelSentiment[] hotelSentiments;
    public RatingAsync(RatingResponse response){
        this.response=response;
    }

    @Override
    protected String doInBackground(String... strings) {

        Amadeus amadeus = Amadeus
                .builder("NvRBVcE8owKujtAeJdqYawFisEWujOZP","f4tG8oUl6znvuJlx")
                .build();

        try {
             hotelSentiments = amadeus.ereputation.hotelSentiments.get(Params.with("hotelIds", new String[]{"ADNYCCTB"}));
        } catch (ResponseException e) {
            e.printStackTrace();
        }

        if (hotelSentiments[0].getResponse().getStatusCode() != 200) {
            System.out.println("Wrong status code: " + hotelSentiments[0].getResponse().getStatusCode());
        }

        return "s";
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.d("Tag",s);
        try {
            response.processFinish(hotelSentiments);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
