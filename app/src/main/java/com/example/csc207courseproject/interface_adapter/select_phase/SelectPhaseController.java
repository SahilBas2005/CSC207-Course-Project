package com.example.csc207courseproject.interface_adapter.select_phase;

import com.example.csc207courseproject.use_case.select_phase.SelectPhaseInputBoundary;
import com.example.csc207courseproject.use_case.select_phase.SelectPhaseInputData;

/**
 * The controller for the Select Phase Use case
 */
public class SelectPhaseController {

    private final SelectPhaseInputBoundary selectphaseUseCaseInteractor;

    public SelectPhaseController(SelectPhaseInputBoundary selectphaseUseCaseInteractor) {
        this.selectphaseUseCaseInteractor = selectphaseUseCaseInteractor;
    }

    /**
     * Execute the Select Phase use case
     */
    public void execute(String selectedPhase) {
        selectphaseUseCaseInteractor.execute(new SelectPhaseInputData(selectedPhase));
    }
}
