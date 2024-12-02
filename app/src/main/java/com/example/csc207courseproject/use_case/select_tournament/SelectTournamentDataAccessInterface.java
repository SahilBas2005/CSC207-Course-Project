package com.example.csc207courseproject.use_case.select_tournament;

import java.util.List;

import org.json.JSONException;

/**
 * DAI for the Select Tournament Use Case.
 */
public interface SelectTournamentDataAccessInterface {
    /**
     * Get the events for a given tournament.
     * @param tournamentID the id of the tournament
     * @return A list containing event names (index 0) and event IDs (index 1)
     * @throws JSONException if there is a problem with the JSON API
     */
    List<List> getEventsInTournament(Integer tournamentID) throws JSONException;
}
