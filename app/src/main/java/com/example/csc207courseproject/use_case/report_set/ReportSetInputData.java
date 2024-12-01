
package com.example.csc207courseproject.use_case.report_set;

import com.example.csc207courseproject.entities.Game;
import com.example.csc207courseproject.entities.SetData;

import java.util.List;

/**
 * The Input Data for the SetReporting Use Case.
 */
public class ReportSetInputData {

    private int setID;
    private int winnerId;
    private SetData currSet;

    private boolean hasDQ;
    public ReportSetInputData(int setID, int winnerId, SetData currSet, boolean hasDQ) {
        this.setID = setID;
        this.winnerId = winnerId;
        this.currSet = currSet;
        this.hasDQ = hasDQ;
    }

    public int getSetID() {
        return setID;
    }

    public void setSetID(int setID) {
        this.setID = setID;
    }

    public int getWinnerId() {
        return winnerId;
    }

    public SetData getCurrSet() {
        return currSet;
    }

    public boolean hasDQ() {
        return hasDQ;
    }
}
