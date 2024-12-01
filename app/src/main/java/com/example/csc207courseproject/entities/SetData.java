package com.example.csc207courseproject.entities;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SetData {

    private int setID;
    private Station station;
    private int firstTo;
    private int winnerID;

    private List<Game> games;
    private Entrant[] players;


    public SetData(int setID, Entrant[] players, int bestOf) {
        this.setID = setID;
        this.players = players;
        this.games = new ArrayList<>();
        games.add(new Game());
        this.firstTo = (bestOf + 1) / 2;

    }

    public int getSetID() {return setID;}

    public void setWinnerID(int winnerID) {this.winnerID = winnerID;}
    public int getWinnerID() {return winnerID;}

    public Station getStation() {return station;}

    public void setStation(Station station) {this.station = station;}

    public Entrant[] getPlayers() {return players;}

    public int getFirstTo() {
        return firstTo;
    }

    public Game getGame(int gameNumber) { return games.get(gameNumber - 1);}

    public List<Game> getGames() {return games;}

    /**
     * Checks if a set conflicts with the parameter station.
     * @param station The station being checked.
     * @return True if the set conflicts
     */
    public boolean conflictsWithStation(Station station){
        for (Entrant entrant : players){
            for(String playerTag : entrant.getTags()){
                if (station.hasTag(playerTag)){
                    return true;
                }
            }
        }
        return false;
    }

    @NotNull
    @Override
    public String toString() {
        return players[0].toString() + " vs. " + players[1].toString();
    }

}