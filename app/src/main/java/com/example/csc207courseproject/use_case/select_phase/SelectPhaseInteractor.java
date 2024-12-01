package com.example.csc207courseproject.use_case.select_phase;

import com.example.csc207courseproject.data_access.api.APIDataAccessException;
import com.example.csc207courseproject.entities.EventData;

public class SelectPhaseInteractor implements SelectPhaseInputBoundary {

    private final SelectPhaseDataAccessInterface dataAccess;
    private final SelectPhaseOutputBoundary selectPhasePresenter;

    public SelectPhaseInteractor(SelectPhaseDataAccessInterface dataAccess, SelectPhaseOutputBoundary selectPhasePresenter) {
        this.dataAccess = dataAccess;
        this.selectPhasePresenter = selectPhasePresenter;
    }

    @Override
    public void execute(SelectPhaseInputData inputData) {
        try {
            int phaseID = EventData.getEventData().getPhaseIds().get(inputData.getPhaseName());
            SelectPhaseOutputData s = new SelectPhaseOutputData(dataAccess.getSeedingInPhase(phaseID));
            selectPhasePresenter.prepareSuccessView(s);
        } catch (APIDataAccessException e) {
            selectPhasePresenter.prepareFailView();
        }
    }
}
