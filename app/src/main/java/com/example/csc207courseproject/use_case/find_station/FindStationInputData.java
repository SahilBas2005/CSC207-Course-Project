package com.example.csc207courseproject.use_case.find_station;

import com.example.csc207courseproject.entities.SetData;
import com.example.csc207courseproject.entities.Station;

import java.util.List;

/**
 * The Input Data for the find station Use Case.
 */
public class FindStationInputData {

    private final SetData currentSet;
    private final List<Station> stations;

    public FindStationInputData(SetData currentSet, List<Station> stations) {
        this.currentSet = currentSet;
        this.stations = stations;
    }

    public SetData getCurrentSet() {return currentSet;}

    public List<Station> getStations() {return stations;}

}
