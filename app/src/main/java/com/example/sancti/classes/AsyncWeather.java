package com.example.sancti.classes;

import android.os.AsyncTask;
import android.util.Log;

import com.example.sancti.interfaces.WeatherResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class AsyncWeather extends AsyncTask<String,Void,String> {

    public WeatherResponse response = null;
    ArrayList<Weather> weathers;

    public AsyncWeather(WeatherResponse response){
        this.response=response;
        weathers=new ArrayList<Weather>();
    }

    @Override
    protected String doInBackground(String... strings) {

        String result="";
        URL url;
        HttpURLConnection urlConnection=null;

        try{
            url=new URL(strings[0]);
            urlConnection=(HttpURLConnection) url.openConnection();
            InputStream in=urlConnection.getInputStream();
            InputStreamReader reader=new InputStreamReader(in);
            int data=reader.read();
            while(data !=-1){
                char current=(char)data;
                result+=current;
                data=reader.read();
            }
            JSONObject object=new JSONObject(result).getJSONObject("forecast");
            JSONArray array=object.getJSONArray("forecastday");
            int count=0;
            JSONObject obj=array.getJSONObject(count);
            while (obj!=null){

                String date= obj.getString("date");
                String max= obj.getJSONObject("day").getString("maxtemp_c");
                String min=obj.getJSONObject("day").getString("mintemp_c");
                String condition= obj.getJSONObject("day").getJSONObject("condition").getString("text");
                String icon= obj.getJSONObject("day").getJSONObject("condition").getString("icon");

                Log.d("weather",icon);
                weathers.add(new Weather(date,min,max,condition,"http:"+icon));

                count++;
                try{
                    obj=array.getJSONObject(count);
                }catch (Exception e){
                    break;
                }

            }


        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        response.processFinish(weathers);
    }
}
