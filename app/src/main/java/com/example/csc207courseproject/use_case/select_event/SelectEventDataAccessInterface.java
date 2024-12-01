package com.example.csc207courseproject.use_case.select_event;

import java.util.List;

import org.json.JSONException;

/**
 * DAI for the Select Event Use Case.
 */
public interface SelectEventDataAccessInterface {
    /**
     * Get the events for a given tournament.
     * @param tournamentID the id of the tournament
     * @return A list containing event names (index 0) and event IDs (index 1)
     * @throws JSONException if there is a problem with the JSON API
     */
    List<List> getEventsInTournament(Integer tournamentID) throws JSONException;

    /**
     * Get all the event data required by the EventData entity for the given event.
     * @param eventID The ID of the event.
     * @return A list containing entrants (index 0), participants (index 1), characters (index 2),
     *      and phase IDs (index 3).
     */
    List<Object> getEventData(Integer eventID);
}
