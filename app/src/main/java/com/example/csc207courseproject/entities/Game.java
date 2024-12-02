package com.example.csc207courseproject.entities;


public class Game {

    private Integer winnerID;

    private String player1Character;
    private String player2Character;


    public Game() {
        player1Character = "No Character";
        player2Character = "No Character";
        winnerID = 0;
    }

    public void setWinnerID(Integer winnerID) {
        this.winnerID = winnerID;
    }

    public void setPlayer1Character(String player1Character) {
        this.player1Character = player1Character;
    }

    public String getPlayer1Character() {
        return player1Character;
    }

    public String getPlayer2Character() {
        return player2Character;
    }

    public void setPlayer2Character(String player2Character) {
            this.player2Character = player2Character;
    }

    public Integer getWinnerID() {
        return winnerID;
    }
}
