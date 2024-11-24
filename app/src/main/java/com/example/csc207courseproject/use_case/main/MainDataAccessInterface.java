package com.example.csc207courseproject.use_case.main;

import com.example.csc207courseproject.entities.Entrant;

/**
 *  The DAO for API calls that must be run on startup
 */
public interface MainDataAccessInterface {

    /**
     * Get entrants in a given event.
     * @param eventID The ID of the event
     * @return A list of entrants
     */
    Entrant[] getEntrantsInEvent(int eventID);
}
