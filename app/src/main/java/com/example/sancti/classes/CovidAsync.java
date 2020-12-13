package com.example.sancti.classes;

import android.os.AsyncTask;
import android.util.Log;


import com.example.sancti.interfaces.CovidResponse;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CovidAsync extends AsyncTask<String,Void,String> {

    public CovidResponse response = null;
    String[] covidState;

    public CovidAsync(CovidResponse response){
        this.response=response;
        covidState=new String[3];
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
            JSONArray array=new JSONArray(result);
            JSONObject json=array.getJSONObject(array.length()-1);
            if(strings[1].equals("1")){
                return json.getString("positive")+" "+ json.getString("death")+" "+json.getString("recovered");
            }else{
                return json.getString("Confirmed")+" "+ json.getString("Deaths")+" "+json.getString("Recovered");
            }

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        covidState=s.split("\\s");
        response.processFinish(covidState);
    }
}
