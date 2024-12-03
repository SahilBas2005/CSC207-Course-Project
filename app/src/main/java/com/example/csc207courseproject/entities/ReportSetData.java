package com.example.csc207courseproject.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * A subtype of the set data class for specific use in reporting sets.
 */
public class ReportSetData extends SetData {

    private final int firstTo;
    private int winnerID;

    private final List<Game> games;


    public ReportSetData(int setID, Entrant[] players, int bestOf) {
        super(setID, players);
        this.games = new ArrayList<>();
        games.add(new Game());
        this.firstTo = (bestOf + 1) / 2;

    }

    public void setWinnerID(int winnerID) {this.winnerID = winnerID;}

    public int getWinnerID() {return winnerID;}

    public int getFirstTo() {
        return firstTo;
    }

    public Game getGame(int gameNumber) { return games.get(gameNumber - 1);}

    public List<Game> getGames() {return games;}

}