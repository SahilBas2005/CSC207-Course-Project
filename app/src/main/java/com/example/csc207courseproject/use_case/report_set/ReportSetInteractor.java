package com.example.csc207courseproject.use_case.report_set;

import com.example.csc207courseproject.data_access.api.APIDataAccessException;
import com.example.csc207courseproject.entities.Game;
import com.example.csc207courseproject.entities.ReportSetData;

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
            ReportSetData currSet = reportSetInputData.getCurrSet();

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

        } catch (APIDataAccessException e){
            reportSetPresenter.prepareFailView("apicallerror");
        }

    }


}
