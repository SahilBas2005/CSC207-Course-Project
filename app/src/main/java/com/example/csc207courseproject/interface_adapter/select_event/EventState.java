package com.example.csc207courseproject.interface_adapter.select_event;

import java.util.List;

/**
 * A state containing the events to be displayed to the user in the Select Event view.
 */
public class EventState {
    private Integer tournamentId;
    private List<String> eventNames;
    private List<Integer> eventIds;

    /**
     * Gets the names of all events within the tournament selected by the user.
     * @return the names of the events
     */
    public List<String> getEventNames() {
        return eventNames;
    }

    /**
     * Sets the names of all events within the tournament selected by the user.
     * @param eventNames the names of the events
     */
    public void setEventNames(List<String> eventNames) {
        this.eventNames = eventNames;
    }

    /**
     * Gets the ids of all events within the tournament selected by the user.
     * @return the ids of the events
     */
    public List<Integer> getEventIds() {
        return eventIds;
    }

    /**
     * Sets the ids of all events within the tournament selected by the user.
     * @param eventIds the ids of the events
     */
    public void setEventIds(List<Integer> eventIds) {
        this.eventIds = eventIds;
    }

    /**
     * Gets the id of the tournament selected by the user.
     * @return id of the tournament
     */
    public Integer getTournamentId() {
        return tournamentId;
    }

    /**
     * Sets the id of the tournament selected by the user.
     * @param tournamentId id of the tournament
     */
    public void setTournamentId(Integer tournamentId) {
        this.tournamentId = tournamentId;
    }
}
