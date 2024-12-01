package com.example.csc207courseproject.use_case.select_event;

import org.json.JSONException;

import java.util.List;

public interface SelectEventDataAccessInterface {
    /**
     * Get the events for a given tournament.
     * @return A list containing a list of event names and a list of event IDs.
     */
    List<List> getEventsInTournament(Integer tournamentID) throws JSONException;

    /**
     * Get all the event data required by the EventData entity for the given event.
     * @param eventID The ID of the event.
     * @return A list containing entrants (index 0), participants (index 1), characters (index 2),
     * and phase IDs (index 3).
     */
    List<Object> getEventData(Integer eventID);
}
