package com.example.csc207courseproject.use_case.report_set;


import android.util.Log;
import com.example.csc207courseproject.entities.Game;
import com.example.csc207courseproject.entities.SetData;

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
            SetData currSet = reportSetInputData.getCurrSet();

            if (winnerID < 0) {
                reportSetPresenter.prepareFailView("incompletesetinfo");
            } else {
                List<Game> games = currSet.getGames();
                boolean isDQ = reportSetInputData.hasDQ();
                int p1EntrantID = currSet.getPlayers()[0].getId();
                int p2EntrantID = currSet.getPlayers()[1].getId();
                dataAccess.reportSet(setID, winnerID, games, isDQ, p1EntrantID, p2EntrantID);
                reportSetPresenter.prepareSuccessView();
            }

        } catch (Exception e){
            reportSetPresenter.prepareFailView("apicallerror");
        }

    }


}
