package com.example.csc207courseproject.use_case.report_game;

import com.example.csc207courseproject.entities.Game;
import com.example.csc207courseproject.entities.SetData;

public class ReportGameInputData {

    private int winnerId;
    private SetData currSet;
    private int gameNumber;
    private String p1Character;
    private String p2Character;

    public ReportGameInputData(SetData currSet, int gameNumber, int winnerId, String p1Character, String p2Character) {
        this.winnerId = winnerId;
        this.currSet = currSet;
        this.gameNumber = gameNumber;
        this.p1Character = p1Character;
        this.p2Character = p2Character;
    }

    public int getWinnerID() { return winnerId; }

    public SetData getCurrSet() { return currSet; }

    public int getGameNumber() { return gameNumber; }

    public String getP1Character() { return p1Character; }

    public String getP2Character() { return p2Character; }

}
