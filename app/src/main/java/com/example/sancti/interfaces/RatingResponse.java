package com.example.sancti.interfaces;


import com.amadeus.resources.HotelSentiment;

public interface RatingResponse {
    void processFinish(HotelSentiment[] output);
}
