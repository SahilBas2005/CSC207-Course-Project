package com.example.csc207courseproject.entities;


public class Game {

    private Integer winnerID;

    private int player1CharacterID;
    private int player2CharacterID;


    public Game() {
        player1CharacterID = -1;
        player2CharacterID = -1;
        winnerID = 0;
    }

    public void setWinnerID(Integer winnerID) {
        this.winnerID = winnerID;
    }

    public void setCharacter(int player, int characterID) {
        if (player == 1) {
            this.player1CharacterID = characterID;
        } else {
            this.player2CharacterID = characterID;
        }
    }

    public Integer getWinnerID() {
        return winnerID;
    }
}
