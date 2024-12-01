package com.example.csc207courseproject.use_case.select_tournament;

public interface SelectTournamentInputBoundary {

    /**
     * Gets all the events in the given tournament.
     * @param selectedTournamentId id of the tournament
     */
    void execute(Integer selectedTournamentId);
}
