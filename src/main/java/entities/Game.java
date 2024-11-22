package entities;

public class Game {

    private int winnerID;
    private int gameNumber;
    private String player1Character;
    private String player2Character;


    public Game(int winnerID, int gameNumber, String player1Character, String player2Character) {
        this.winnerID = winnerID;
        this.gameNumber = gameNumber;
        this.player1Character = player1Character;
        this.player2Character = player2Character;
    }
}
