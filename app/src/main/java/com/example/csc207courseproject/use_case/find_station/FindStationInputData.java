package com.example.csc207courseproject.use_case.find_station;

import com.example.csc207courseproject.entities.CallSetData;
import com.example.csc207courseproject.entities.Station;

import java.util.List;

/**
 * The Input Data for the find station Use Case.
 */
public class FindStationInputData {

    private final CallSetData currentSet;
    private final List<Station> stations;

    public FindStationInputData(CallSetData currentSet, List<Station> stations) {
        this.currentSet = currentSet;
        this.stations = stations;
    }

    public CallSetData getCurrentSet() {return currentSet;}

    public List<Station> getStations() {return stations;}

}
