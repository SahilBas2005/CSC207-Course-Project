package com.example.csc207courseproject.use_case.select_event;

public interface SelectEventInputBoundary {
    /**
     * Stores the event data in the EventData entity.
     * @param tournamentId the tournament that the event belongs to
     * @param selectedEventName name of the event
     * @param selectedEventID id of the event
     */
    void execute(Integer tournamentId, String selectedEventName, Integer selectedEventID);
}
