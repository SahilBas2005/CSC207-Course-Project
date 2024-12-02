package com.example.csc207courseproject.use_case.select_tournament;

/**
 * Input boundary for the Select Tournament Use Case.
 */
public interface SelectTournamentInputBoundary {

    /**
     * Gets all the events in the given tournament.
     * @param selectedTournamentId id of the tournament
     */
    void execute(Integer selectedTournamentId);
}
