package com.example.csc207courseproject.use_case.report_game;

import com.example.csc207courseproject.entities.Entrant;
import com.example.csc207courseproject.entities.Game;
import com.example.csc207courseproject.entities.ReportSetData;
import org.junit.jupiter.api.Test;

class ReportGameInteractorTest {

    @Test
    void completeSetTest() {

        // Create example input data of an almost completed set
        Entrant en1 = new Entrant(null, 1);
        Entrant en2 = new Entrant(null, 2);
        ReportSetData exampleSet = new ReportSetData(1, new Entrant[] {en1, en2}, 5);

        // Set the Set Game Count to be 2-0 in favour of P1
        exampleSet.getGame(1).setWinnerID(1);
        Game g2 = new Game();
        Game g3 = new Game();
        g2.setWinnerID(1);
        exampleSet.getGames().add(g2);
        exampleSet.getGames().add(g3);

        // Report a game 3 win for player 1, ending the set
        ReportGameInputData inputData = new ReportGameInputData(exampleSet, 3, 1);

        // Create test presenter
        ReportGameOutputBoundary setOverPresenter = outputData -> {
            assert outputData.getSetOver();
        };

        // Execute the interactor method
        ReportGameInputBoundary interactor = new ReportGameInteractor(setOverPresenter);
        interactor.execute(inputData);
    }

    @Test
    void incompleteSetGamesTest() {

        // Create example input data of an incomplete set
        Entrant en1 = new Entrant(null, 1);
        Entrant en2 = new Entrant(null, 2);
        ReportSetData exampleSet = new ReportSetData(1, new Entrant[]{en1, en2}, 5);

        Game g2 = new Game();
        Game g3 = new Game();
        exampleSet.getGame(1).setWinnerID(1);
        g2.setWinnerID(2);
        exampleSet.getGames().add(g2);
        exampleSet.getGames().add(g3);

        ReportGameInputData inputData = new ReportGameInputData(exampleSet, 3, 1);

        // Create test presenter
        ReportGameOutputBoundary setNotOverPresenter = outputData -> {
            assert !outputData.getSetOver();
        };

        ReportGameInputBoundary interactor = new ReportGameInteractor(setNotOverPresenter);
        interactor.execute(inputData);
    }

    @Test
    void removeLoserUselessGamesTest() {

        // Create example reportset data
        Entrant en1 = new Entrant(null, 1);
        Entrant en2 = new Entrant(null, 2);
        ReportSetData exampleSet = new ReportSetData(1, new Entrant[]{en1, en2}, 5);

        // Create a set where P2 wins in game 4 and 5 should be removed from the games list,
        // as by game 3, P1 has won.
        Game g2 = new Game();
        Game g3 = new Game();
        Game g4 = new Game();
        Game g5 = new Game();
        exampleSet.getGame(1).setWinnerID(1);
        g2.setWinnerID(2);
        g3.setWinnerID(1);
        g4.setWinnerID(2);
        g5.setWinnerID(2);

        // Add games to the example set
        exampleSet.getGames().add(g2);
        exampleSet.getGames().add(g3);
        exampleSet.getGames().add(g4);
        exampleSet.getGames().add(g5);

        ReportGameInputData inputData = new ReportGameInputData(exampleSet, 2, 1);

        // Create test presenter
        ReportGameOutputBoundary setPresenter = outputData -> {
            // Test that game 4 and 5 have been removed from the games list
            assert outputData.getP1Score() + outputData.getP2Score() == 3;
        };

        ReportGameInputBoundary interactor = new ReportGameInteractor(setPresenter);
        interactor.execute(inputData);
    }

    @Test
    void removeWinnerUselessGameTest() {

        // Create example input data of an incomplete set
        Entrant en1 = new Entrant(null, 1);
        Entrant en2 = new Entrant(null, 2);
        ReportSetData exampleSet = new ReportSetData(1, new Entrant[]{en1, en2}, 5);

        Game g2 = new Game();
        Game g3 = new Game();
        Game g4 = new Game();
        exampleSet.getGame(1).setWinnerID(1);
        g2.setWinnerID(2);
        g3.setWinnerID(1);
        g4.setWinnerID(1);

        // Add games to the example set
        exampleSet.getGames().add(g2);
        exampleSet.getGames().add(g3);
        exampleSet.getGames().add(g4);

        ReportGameInputData inputData = new ReportGameInputData(exampleSet, 2, 1);

        // Create test presenter
        ReportGameOutputBoundary setPresenter = outputData -> {
            // Test that game 4 has been removed from the games list
            assert outputData.getP1Score() + outputData.getP2Score() == 3;
        };

        ReportGameInputBoundary interactor = new ReportGameInteractor(setPresenter);
        interactor.execute(inputData);
    }
}
