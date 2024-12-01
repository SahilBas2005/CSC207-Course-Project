package com.example.csc207courseproject.use_case.call_set;

import com.example.csc207courseproject.entities.Station;

/**
 * The Input Data for the call set Use Case.
 */
public class CallSetInputData {

    private final int setId;
    private final Station station;

    public CallSetInputData(int setId, Station station) {
        this.setId = setId;
        this.station = station;
    }

    public int getSetId() {return setId;}

    public Station getStation() {return station;}

}
