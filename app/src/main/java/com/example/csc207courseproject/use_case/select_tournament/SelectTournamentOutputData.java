package com.example.csc207courseproject.use_case.select_tournament;

import java.util.List;

/**
 * Output data for the Select Tournament Use Case.
 */
public class SelectTournamentOutputData {

    private final Integer tournamentId;
    private final List<String> eventNames;
    private final List<Integer> eventIds;

    /**
     * The class constructor.
     * @param tournamentId id of the selected tournament
     * @param events a list containing names and ids of events within the selected tournament
     */
    public SelectTournamentOutputData(Integer tournamentId, List<List> events) {
        this.tournamentId = tournamentId;
        eventNames = events.get(0);
        eventIds = events.get(1);
    }

    /**
     * Gets the names of the events in the selected tournament.
     * @return a list of event names
     */
    public List<String> getEventNames() {
        return eventNames;
    }

    /**
     * Gets the ids of the events in the selected tournament.
     * @return a list of event ids
     */
    public List<Integer> getEventIds() {
        return eventIds;
    }

    /**
     * Gets the id of the selected tournament.
     * @return the id of the tournament
     */
    public Integer getTournamentId() {
        return tournamentId;
    }
}
