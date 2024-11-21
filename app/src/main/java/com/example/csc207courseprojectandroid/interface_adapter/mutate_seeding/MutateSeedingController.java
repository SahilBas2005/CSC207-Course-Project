package com.example.csc207courseprojectandroid.interface_adapter.mutate_seeding;

import com.example.csc207courseprojectandroid.interface_adapter.update_seeding.SeedingState;
import com.example.csc207courseprojectandroid.use_case.mutate_seeding.MutateSeedingInputBoundary;
import com.example.csc207courseprojectandroid.use_case.mutate_seeding.MutateSeedingInputData;

import java.util.List;

/**
 * The controller for the Select Phase Use case
 */
public class MutateSeedingController {

    private final MutateSeedingInputBoundary mutateSeedingUseCaseInteractor;
    private final SeedingState seedingState;

    public MutateSeedingController(MutateSeedingInputBoundary mutateSeedingUseCaseInteractor, SeedingState seedingState) {
        this.mutateSeedingUseCaseInteractor = mutateSeedingUseCaseInteractor;
        this.seedingState = seedingState;
    }

    /**
     * Execute the Select Phase use case
     */
    public void execute() {

        List<Integer> seeding = seedingState.getSeeding();
        final MutateSeedingInputData selectPhaseInputData = new MutateSeedingInputData(seeding);

        mutateSeedingUseCaseInteractor.execute(selectPhaseInputData);
    }
}

