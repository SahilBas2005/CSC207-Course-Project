package com.example.csc207courseproject.use_case.report_set;


import android.util.Log;
import com.example.csc207courseproject.entities.Game;

import java.util.List;

public class ReportSetInteractor implements ReportSetInputBoundary {

    private final ReportSetDataAccessInterface dataAccess;

    private final ReportSetOutputBoundary reportSetPresenter;

    public ReportSetInteractor(ReportSetDataAccessInterface dataAccess, ReportSetOutputBoundary reportSetPresenter) {
        this.dataAccess = dataAccess;
        this.reportSetPresenter = reportSetPresenter;
    }

    @Override
    public void execute(ReportSetInputData reportSetInputData) {


        try {
            int setID = reportSetInputData.getSetID();
            int winnerID = reportSetInputData.getWinnerId();

            if (winnerID < 0) {
                reportSetPresenter.prepareFailView("incompletesetinfo");
            } else {
                List<Game> games = reportSetInputData.getGames();
                boolean isDQ = reportSetInputData.hasDQ();
                dataAccess.reportSet(setID, winnerID, games, isDQ);
                reportSetPresenter.prepareSuccessView();
            }

        } catch (Exception e){
            reportSetPresenter.prepareFailView("apicallerror");
        }

    }


}
