package com.example.sancti.interfaces;


import com.example.sancti.classes.Weather;

import java.util.ArrayList;

public interface WeatherResponse {
    void processFinish(ArrayList<Weather> output);
}
