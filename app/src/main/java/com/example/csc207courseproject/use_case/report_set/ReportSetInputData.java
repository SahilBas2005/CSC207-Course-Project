
package com.example.csc207courseproject.use_case.report_set;

import com.example.csc207courseproject.entities.Game;

import java.util.List;

/**
 * The Input Data for the SetReporting Use Case.
 */
public class ReportSetInputData {

    private int setID;
    private int winnerId;
    private List<Game> games;

    private boolean hasDQ;
    public ReportSetInputData(int setID, int winnerId, List<Game> games, boolean hasDQ) {
        this.setID = setID;
        this.winnerId = winnerId;
        this.games = games;
        this.hasDQ = hasDQ;
    }

    public int getSetID() {
        return setID;
    }

    public void setSetID(int setID) {
        this.setID = setID;
    }

    public int getWinnerId() {
        return winnerId;
    }

    public List<Game> getGames() {
        return games;
    }

    public boolean hasDQ() {
        return hasDQ;
    }
}
