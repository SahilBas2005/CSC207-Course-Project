package com.example.csc207courseproject.use_case.report_game;

import com.example.csc207courseproject.entities.ReportSetData;

public class ReportGameInputData {

    private int winnerId;
    private ReportSetData currSet;
    private int gameNumber;

    public ReportGameInputData(ReportSetData currSet, int gameNumber, int winnerId) {
        this.winnerId = winnerId;
        this.currSet = currSet;
        this.gameNumber = gameNumber;
    }

    public int getWinnerID() { return winnerId; }

    public ReportSetData getCurrSet() { return currSet; }

    public int getGameNumber() { return gameNumber; }


}
