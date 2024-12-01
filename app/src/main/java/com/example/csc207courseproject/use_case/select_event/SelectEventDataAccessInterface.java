package com.example.csc207courseproject.use_case.select_event;

import java.util.List;

/**
 * DAI for the Select Event Use Case.
 */
public interface SelectEventDataAccessInterface {
    /**
     * Get all the event data required by the EventData entity for the given event.
     * @param eventID The ID of the event.
     * @return A list containing entrants (index 0), participants (index 1), characters (index 2),
     *      and phase IDs (index 3).
     */
    List<Object> getEventData(Integer eventID);
}
