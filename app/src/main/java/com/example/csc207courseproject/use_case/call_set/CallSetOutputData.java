package com.example.csc207courseproject.use_case.call_set;

import com.example.csc207courseproject.entities.Station;

public class CallSetOutputData {

    private final Station station;


    public CallSetOutputData(Station station) {
        this.station = station;
    }

    public Station getStation() {
        return station;
    }
}
