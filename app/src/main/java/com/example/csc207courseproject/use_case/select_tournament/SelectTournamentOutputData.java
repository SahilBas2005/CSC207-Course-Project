package com.example.csc207courseproject.use_case.select_tournament;

import java.util.List;

/**
 * Output data for the Select Tournament Use Case.
 */
public class SelectTournamentOutputData {

    private final Integer tournamentId;
    private final List<String> eventNames;
    private final List<Integer> eventIds;

    public SelectTournamentOutputData(Integer tournamentId, List<List> events) {
        this.tournamentId = tournamentId;
        eventNames = events.get(0);
        eventIds = events.get(1);
    }

    public List<String> getEventNames() {
        return eventNames;
    }

    public List<Integer> getEventIds() {
        return eventIds;
    }

    public Integer getTournamentId() {
        return tournamentId;
    }
}
