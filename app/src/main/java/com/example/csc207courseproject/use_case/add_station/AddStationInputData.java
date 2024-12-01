package com.example.csc207courseproject.use_case.add_station;

/**
 * The Input Data for the add station Use Case.
 */
public class AddStationInputData {

    private final int stationNumber;

    public AddStationInputData(int stationNumber) {
        this.stationNumber = stationNumber;
    }

    public int getStationNumber() {return stationNumber;}

}
