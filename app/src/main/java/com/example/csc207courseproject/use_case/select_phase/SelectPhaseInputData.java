package com.example.csc207courseproject.use_case.select_phase;

/**
 * The Input Data for the Select Phase Use Case.
 */
public class SelectPhaseInputData {

    private final String phaseName;

    public SelectPhaseInputData(String phaseName) {
        this.phaseName = phaseName;
    }

    public String getPhaseName() {
        return phaseName;
    }
}
