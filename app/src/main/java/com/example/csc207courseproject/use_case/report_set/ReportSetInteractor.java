package com.example.csc207courseproject.use_case.report_set;

import java.util.List;

import com.example.csc207courseproject.data_access.DataAccessException;
import com.example.csc207courseproject.entities.Game;
import com.example.csc207courseproject.entities.ReportSetData;

/**
 * The Interactor for the Report Set Use Case.
 */
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
            final int setID = reportSetInputData.getSetID();
            final int winnerID = reportSetInputData.getWinnerId();
            final ReportSetData currSet = reportSetInputData.getCurrSet();

            if (winnerID < 0) {
                reportSetPresenter.prepareFailView("incompletesetinfo");
            } else {
                final List<Game> games = currSet.getGames();
                final boolean isDQ = reportSetInputData.hasDQ();
                final int p1EntrantID = currSet.getPlayers()[0].getId();
                final int p2EntrantID = currSet.getPlayers()[1].getId();
                dataAccess.reportSet(setID, winnerID, games, isDQ, p1EntrantID, p2EntrantID);
                reportSetPresenter.prepareSuccessView();
            }

        } catch (DataAccessException e) {
            reportSetPresenter.prepareFailView("apicallerror");
        }

    }

}
