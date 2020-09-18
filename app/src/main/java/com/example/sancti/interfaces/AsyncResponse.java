package com.example.sancti.interfaces;

import com.amadeus.resources.FlightOfferSearch;
import com.amadeus.resources.HotelOffer;

public interface AsyncResponse {
    void processFinish(HotelOffer[] output);
    void processFinish(FlightOfferSearch[] output);
}
