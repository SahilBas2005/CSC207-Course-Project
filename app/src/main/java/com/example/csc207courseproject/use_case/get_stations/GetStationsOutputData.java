package com.example.csc207courseproject.use_case.get_stations;

import com.example.csc207courseproject.entities.Station;

import java.util.List;

public class GetStationsOutputData {

    private final List<Station> stations;


    public GetStationsOutputData(List<Station> stations) {
        this.stations = stations;
    }

    public List<Station> getStations() {
        return stations;
    }
}
