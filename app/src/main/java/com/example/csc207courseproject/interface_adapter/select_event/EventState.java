package com.example.csc207courseproject.interface_adapter.select_event;

import java.util.List;

/**
 * A state containing the events to be displayed to the user in the Select Event view.
 */
public class EventState {
    private Integer tournamentId;
    private List<String> eventNames;
    private List<Integer> eventIds;

    public List<String> getEventNames() {
        return eventNames;
    }

    public void setEventNames(List<String> eventNames) {
        this.eventNames = eventNames;
    }

    public List<Integer> getEventIds() {
        return eventIds;
    }

    public void setEventIds(List<Integer> eventIds) {
        this.eventIds = eventIds;
    }

    public Integer getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(Integer tournamentId) {
        this.tournamentId = tournamentId;
    }
}
