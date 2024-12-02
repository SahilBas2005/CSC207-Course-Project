
package com.example.csc207courseproject.use_case.report_set;

import com.example.csc207courseproject.entities.ReportSetData;

/**
 * The Input Data for the Report Set Use Case.
 */
public class ReportSetInputData {

    private int setID;
    private int winnerId;
    private ReportSetData currSet;

    private boolean hasDQ;

    public ReportSetInputData(int setID, int winnerId, ReportSetData currSet, boolean hasDQ) {
        this.setID = setID;
        this.winnerId = winnerId;
        this.currSet = currSet;
        this.hasDQ = hasDQ;
    }

    public int getSetID() {
        return setID;
    }

    public int getWinnerId() {
        return winnerId;
    }

    public ReportSetData getCurrSet() {
        return currSet;
    }

    public boolean hasDQ() {
        return hasDQ;
    }
}
