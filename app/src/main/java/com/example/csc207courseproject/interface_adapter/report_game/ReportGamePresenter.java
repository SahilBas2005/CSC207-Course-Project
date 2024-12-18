package com.example.csc207courseproject.interface_adapter.report_game;

import com.example.csc207courseproject.entities.Game;
import com.example.csc207courseproject.ui.report.ReportViewModel;
import com.example.csc207courseproject.use_case.report_game.ReportGameOutputBoundary;
import com.example.csc207courseproject.use_case.report_game.ReportGameOutputData;
import com.example.csc207courseproject.interface_adapter.report_set.ReportSetState;

import java.util.ArrayList;
import java.util.List;


public class ReportGamePresenter implements ReportGameOutputBoundary {

    private final ReportViewModel reportViewModel;

    public ReportGamePresenter(ReportViewModel gameViewModel) {
        this.reportViewModel = gameViewModel;
    }

    @Override
    public void prepareSuccessView(ReportGameOutputData outputData) {
        final ReportSetState reportSetState = reportViewModel.getState();
        //Update the state with the information the view needs
        // Scores for both players
        // button presses need to be stored
        // Character stuff eventually
        int p1Score = outputData.getP1Score();
        int p2Score = outputData.getP2Score();
        boolean setOver = outputData.getSetOver();

        List<Boolean> newp1ButtonPresses = updateWinnerBools(reportSetState.getCurrentSet().getGames(),
                reportSetState.getCurrentSet().getPlayers()[0].getId());
        List<Boolean> newp2ButtonPresses = updateWinnerBools(reportSetState.getCurrentSet().getGames(),
                reportSetState.getCurrentSet().getPlayers()[1].getId());

        reportSetState.setP1ButtonPresses(newp1ButtonPresses);
        reportSetState.setP2ButtonPresses(newp2ButtonPresses);

        reportSetState.setP1Wins(p1Score);
        reportSetState.setP2Wins(p2Score);

        reportSetState.setSetOver(setOver);

        reportViewModel.firePropertyChanged("reportgamesuccess");
    }

    private List<Boolean> updateWinnerBools(List<Game> games, int winnerID) {
        List<Boolean> newButtonPresses = new ArrayList<>();
        for (Game game : games) {
            newButtonPresses.add(game.getWinnerID() == winnerID);
        }
        return newButtonPresses;
    }

}
