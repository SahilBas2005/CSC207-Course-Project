package com.example.csc207courseproject.use_case.ongoing_sets;

import com.example.csc207courseproject.entities.SetData;

import java.util.List;

/**
 * The DAO for the ongoing sets use case.
 */
public interface OngoingSetsDataAccessInterface {

    /**
     * Returns the sets that are currently being played and can be reported.
     * @param eventId The ID of the event
     * @return The upcoming sets
     */
    public List<SetData> getOngoingSets(int eventId);
}
