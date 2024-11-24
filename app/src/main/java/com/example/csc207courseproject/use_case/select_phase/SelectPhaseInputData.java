package com.example.csc207courseproject.use_case.select_phase;

/**
 * The Input Data for the Select Phase Use Case.
 */
public class SelectPhaseInputData {

    private int phaseID;

    public SelectPhaseInputData(int phaseID) {
        this.phaseID = phaseID;
    }

    public int getPhaseID() {
        return phaseID;
    }

    public void setPhaseName(int phaseID) {
        this.phaseID = phaseID;
    }
}
