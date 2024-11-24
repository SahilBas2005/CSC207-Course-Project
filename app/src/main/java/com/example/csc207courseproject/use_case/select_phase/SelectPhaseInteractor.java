package com.example.csc207courseproject.use_case.select_phase;

import android.util.Log;

import java.util.Arrays;

public class SelectPhaseInteractor implements SelectPhaseInputBoundary {

    private final SelectPhaseDataAccessInterface dataAccess;
    private final SelectPhaseOutputBoundary selectPhasePresenter;

    public SelectPhaseInteractor(SelectPhaseDataAccessInterface dataAccess, SelectPhaseOutputBoundary selectPhasePresenter) {
        this.dataAccess = dataAccess;
        this.selectPhasePresenter = selectPhasePresenter;
    }

    @Override
    public void execute(SelectPhaseInputData selectPhaseInputData) {
        int phaseID = selectPhaseInputData.getPhaseID();
        try {
            SelectPhaseOutputData s = new SelectPhaseOutputData(dataAccess.getSeedingInPhase(phaseID));
            selectPhasePresenter.prepareSuccessView(s);
        } catch (Exception e) {
            Log.d("Exception", Arrays.toString(e.getStackTrace()));
            selectPhasePresenter.prepareFailView("Something went wrong with the API call, try again.");
        }
    }
}
