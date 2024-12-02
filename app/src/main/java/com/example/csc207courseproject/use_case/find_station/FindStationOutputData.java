package com.example.csc207courseproject.use_case.find_station;

import com.example.csc207courseproject.entities.Station;

public class FindStationOutputData {

    private final Station station;
    private Station openStream;


    public FindStationOutputData(Station station, Station openStream) {
        this.station = station;
        this.openStream = openStream;
    }

    public Station getStation() {
        return station;
    }

    public Station getOpenStream() {return openStream;}
}
