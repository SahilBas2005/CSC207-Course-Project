package com.example.csc207courseproject.use_case.report_game;

import com.example.csc207courseproject.entities.Game;
import com.example.csc207courseproject.entities.SetData;

public class ReportGameInputData {

    private int winnerId;
    private SetData currSet;
    private int gameNumber;

    public ReportGameInputData(SetData currSet, int gameNumber, int winnerId) {
        this.winnerId = winnerId;
        this.currSet = currSet;
        this.gameNumber = gameNumber;
    }

    public int getWinnerID() { return winnerId; }

    public SetData getCurrSet() { return currSet; }

    public int getGameNumber() { return gameNumber; }


}
