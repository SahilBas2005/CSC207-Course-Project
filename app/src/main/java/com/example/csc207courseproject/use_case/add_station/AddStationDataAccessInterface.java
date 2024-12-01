package com.example.csc207courseproject.use_case.add_station;

/**
 * The DAO for the add station use case.
 */
public interface AddStationDataAccessInterface {

    /**
     * Adds a station to an event.
     * @param tournamentId The ID of the event
     * @param stationNumber The number of the station
     * @return The id of the added station
     */
    public int addStation(int tournamentId, int stationNumber);
}
