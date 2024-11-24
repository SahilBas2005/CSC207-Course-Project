package com.example.csc207courseproject.interface_adapter.select_phase;

import com.example.csc207courseproject.interface_adapter.update_seeding.SeedingState;
import com.example.csc207courseproject.use_case.select_phase.SelectPhaseInputBoundary;
import com.example.csc207courseproject.use_case.select_phase.SelectPhaseInputData;

/**
 * The controller for the Select Phase Use case
 */
public class SelectPhaseController {

    private final SelectPhaseInputBoundary selectphaseUseCaseInteractor;
    private final SeedingState seedingState;

    public SelectPhaseController(SelectPhaseInputBoundary selectphaseUseCaseInteractor, SeedingState seedingState) {
        this.selectphaseUseCaseInteractor = selectphaseUseCaseInteractor;
        this.seedingState = seedingState;
    }

    /**
     * Execute the Select Phase use case
     */
    public void execute(String selectedPhase) {

        int phaseID = seedingState.phaseNametoId(selectedPhase);
        final SelectPhaseInputData selectPhaseInputData = new SelectPhaseInputData(phaseID);

        selectphaseUseCaseInteractor.execute(selectPhaseInputData);
    }
}
