package com.example.csc207courseproject.use_case.upcoming_sets;


import com.example.csc207courseproject.entities.CallSetData;

import java.util.List;

/**
 * The DAO for the upcoming sets use case.
 */
public interface UpcomingSetsDataAccessInterface {

    /**
     * Returns the sets that have not been called yet.
     * @param eventId The ID of the event
     * @return The upcoming sets
     */
    public List<CallSetData> getUpcomingSets(int eventId);
}
