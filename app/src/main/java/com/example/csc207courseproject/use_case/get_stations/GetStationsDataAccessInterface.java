package com.example.csc207courseproject.use_case.get_stations;


import com.example.csc207courseproject.entities.Station;

import java.util.List;

/**
 * The DAO for the get stations use case.
 */
public interface GetStationsDataAccessInterface {

    /**
     * Returns the stations assigned to an event.
     * @param eventId The ID of the event
     * @return The stations in a list
     */
    public List<Station> getStations(int eventId);
}
