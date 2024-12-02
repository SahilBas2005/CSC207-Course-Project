
package com.example.csc207courseproject.interface_adapter.report_set;

import com.example.csc207courseproject.use_case.report_set.ReportSetInputBoundary;
import com.example.csc207courseproject.use_case.report_set.ReportSetInputData;

/**
 * The controller for the Report Set Use case.
 */
public class ReportSetController {

    private final ReportSetInputBoundary reportSetUseCaseInteractor;
    private final ReportSetState currState;

    public ReportSetController(ReportSetInputBoundary reportSetUseCaseInteractor, ReportSetState currState) {
        this.reportSetUseCaseInteractor = reportSetUseCaseInteractor;
        this.currState = currState;
    }

    /**
     * Execute the report set use case.
     * @param p1DQ Whether the first player DQ'd
     * @param p2DQ Wherther the second player DQ's
     */
    public void execute(boolean p1DQ, boolean p2DQ) {

        final int setID = currState.getCurrentSet().getSetID();
        final int winnerID;

        // Set the winnerID in the case of DQs
        final boolean hasDQ = p1DQ || p2DQ;

        if (hasDQ) {
            if (p2DQ) {
                winnerID = currState.getCurrentSet().getPlayers()[0].getId();
            } else {
                winnerID = currState.getCurrentSet().getPlayers()[1].getId();
            }
        } else {
            // If the set is not over yet, then there is no current winner
            if (currState.isSetOver()) {
                winnerID = currState.getCurrentSet().getWinnerID();
            } else {
                winnerID = -1;
            }
        }

        final ReportSetInputData reportSetInputData = new ReportSetInputData(setID, winnerID,
                currState.getCurrentSet(), hasDQ);

        reportSetUseCaseInteractor.execute(reportSetInputData);
    }
}
