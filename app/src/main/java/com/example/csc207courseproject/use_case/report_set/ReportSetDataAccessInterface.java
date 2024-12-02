package com.example.csc207courseproject.use_case.report_set;

import java.util.List;

import com.example.csc207courseproject.entities.Game;

/**
 * DAI for the ReportSet Use Case.
 */
public interface ReportSetDataAccessInterface {

    /**
     * Reports the results for a certain match to start.gg through an API call.
     * @param setID The set's ID
     * @param winnerId The entrant ID of the winner
     * @param games List of Game objects which contain information on player IDs and characters
     * @param hasDQ Whether the set has at least 1 DQ
     * @param p1EntrantID The entrant ID of player 1 for the API call
     * @param p2EntrantID The entrant ID of player 2 for the API call
     */
    void reportSet(int setID, int winnerId, List<Game> games, boolean hasDQ, int p1EntrantID, int p2EntrantID);
}
