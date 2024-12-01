package com.example.csc207courseproject.use_case.report_game;

import com.example.csc207courseproject.entities.Entrant;
import com.example.csc207courseproject.entities.Game;
import com.example.csc207courseproject.entities.SetData;

import java.util.ArrayList;
import java.util.List;

public class ReportGameInteractor implements ReportGameInputBoundary {

    private final ReportGameOutputBoundary reportGamePresenter;

    public ReportGameInteractor(ReportGameOutputBoundary reportGamePresenter) {
        this.reportGamePresenter = reportGamePresenter;
    }

    @Override
    public void execute(ReportGameInputData reportGameInputData) {

        int gameWinnerID = reportGameInputData.getWinnerID();
        SetData currSet = reportGameInputData.getCurrSet();
        int gameNumber = reportGameInputData.getGameNumber();
        String p1Character = reportGameInputData.getP1Character();
        String p2Character = reportGameInputData.getP2Character();
        List<Game> games = currSet.getGames();

        currSet.getGame(gameNumber).setWinnerID(gameWinnerID);
        boolean over = isSetOver(currSet);
        //Add or remove games from the menu
        if (!over) {
            //Only add another game to the menu if all the current ones are reported
            if (games.get(games.size() - 1).getWinnerID() != 0) {
                games.add(new Game());
            }
        } else {
            currSet.setWinnerID(gameWinnerID);
            // Remove unnecessary games
            int i = games.size() - 1;
            List<Game> uselessGames = new ArrayList<>();
            while (games.get(i).getWinnerID() != gameWinnerID){
                uselessGames.add(games.get(i));
                i -= 1;
            }
            games.removeAll(uselessGames);

            // If a player now has an extra win, remove the extra win at the end from the games list
            if (countWins(gameWinnerID, games) > currSet.getFirstTo()) {
                games.remove(games.size() - 1);
            }
        }
        int p1ID = currSet.getPlayers()[0].getId();
        int p2ID = currSet.getPlayers()[1].getId();

        reportGamePresenter.prepareSuccessView(new ReportGameOutputData(countWins(p1ID, games), countWins(p2ID, games),
                isSetOver(currSet), p1Character, p2Character));

    }

    //IMPLEMENT THIS WHEN CHARACTER LOGIC IS BETTER
//        public void reportCharacter(int gameNum, int playerNum, String character) {
//            this.games.get(gameNum - 1).setCharacter(playerNum - 1, EventData.getCharacterIds().get(character));
//        }

    private boolean isSetOver(SetData set) {
        Entrant[] players = set.getPlayers();
        return countWins(players[0].getId(), set.getGames()) >= set.getFirstTo() ||
                countWins(players[1].getId(), set.getGames()) >= set.getFirstTo();
    }

    private int countWins(int winnerID, List<Game> games) {
        int count = 0;
        for (Game g : games) {
            if (g.getWinnerID() == winnerID) {
                count++;
            }
        }
        return count;
    }

}
