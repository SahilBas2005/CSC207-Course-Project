package com.example.csc207courseproject.use_case.get_stations;

import com.example.csc207courseproject.entities.Station;

import java.util.List;

/**
 * The Input Data for the Mutate Seeding to Start.gg Use Case.
 */
public class GetStationsInputData {

    private final List<Station> localStations;

    public GetStationsInputData(List<Station> localStations) {
        this.localStations = localStations;
    }

    public List<Station> getlocalStations() {
        return localStations;
    }

}
