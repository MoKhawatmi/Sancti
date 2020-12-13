package com.example.sancti.classes;

import android.os.AsyncTask;


import com.example.sancti.interfaces.photosResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class PhotosAsync extends AsyncTask<String,Void,String> {

    public photosResponse response = null;
    String[] images;

    public PhotosAsync(photosResponse response){
        this.response=response;
        images=new String[6];
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
            JSONObject json=new JSONObject(result);
            JSONArray arr=json.getJSONArray("results");
            for(int i=0;i<6;i++){
                images[i]=arr.getJSONObject(i).getJSONObject("urls").getString("small");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        response.processFinish(images);
    }
}
