package com.example.csc207courseproject.use_case.select_phase;

import android.util.Log;
import com.example.csc207courseproject.data_access.api.APIDataAccessException;

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
        } catch (APIDataAccessException e) {
            selectPhasePresenter.prepareFailView("Something went wrong with the API call, try again.");
        }
    }
}
