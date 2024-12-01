package com.example.csc207courseproject.use_case.add_station;

import com.example.csc207courseproject.entities.Station;

public class AddStationOutputData {

    private final Station station;


    public AddStationOutputData(Station station) {
        this.station = station;
    }

    public Station getStation() {
        return station;
    }
}
