package com.example.csc207courseproject.interface_adapter.report_game;

import com.example.csc207courseproject.interface_adapter.report_set.ReportSetState;
import com.example.csc207courseproject.use_case.report_game.ReportGameInputBoundary;
import com.example.csc207courseproject.use_case.report_game.ReportGameInputData;


public class ReportGameController {
    private final ReportGameInputBoundary reportGameUseCaseInteractor;
    private final ReportSetState currState;

    public ReportGameController(ReportGameInputBoundary reportGameUseCaseInteractor, ReportSetState currState) {
        this.reportGameUseCaseInteractor = reportGameUseCaseInteractor;
        this.currState = currState;
    }


    /**
     * Execute the report game use case
     */
    public void execute(int gameNumber, int winner, String p1Char, String p2Char) {
        int winnerID;

        if (winner == 1) {
            winnerID = currState.getCurrentSet().getPlayers()[0].getId();
        } else {
            winnerID = currState.getCurrentSet().getPlayers()[1].getId();
        }

        reportGameUseCaseInteractor.execute(new ReportGameInputData(currState.getCurrentSet(),
                gameNumber, winnerID, p1Char, p2Char));

    }
}
