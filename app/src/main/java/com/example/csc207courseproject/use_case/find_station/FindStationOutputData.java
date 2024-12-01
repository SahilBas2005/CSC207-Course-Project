package com.example.csc207courseproject.use_case.find_station;

import com.example.csc207courseproject.entities.Station;

public class FindStationOutputData {

    private final Station station;


    public FindStationOutputData(Station station) {
        this.station = station;
    }

    public Station getStation() {
        return station;
    }
}
