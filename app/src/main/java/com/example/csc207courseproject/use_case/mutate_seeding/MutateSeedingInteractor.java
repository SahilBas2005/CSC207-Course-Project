package com.example.csc207courseproject.use_case.mutate_seeding;

import java.util.List;

public class MutateSeedingInteractor implements MutateSeedingInputBoundary {

    private final MutateSeedingDataAccessInterface dataAccess;
    private final MutateSeedingOutputBoundary mutateSeedingPresenter;

    public MutateSeedingInteractor(MutateSeedingDataAccessInterface dataAccess, MutateSeedingOutputBoundary mutateSeedingPresenter) {
        this.dataAccess = dataAccess;
        this.mutateSeedingPresenter = mutateSeedingPresenter;
    }

    @Override
    public void execute(MutateSeedingInputData mutateSeedingInputData) {
        List<Integer> finalSeeds = mutateSeedingInputData.getFinalSeeds();

        try {
            dataAccess.setSeeding(finalSeeds);
            mutateSeedingPresenter.prepareSuccessView();
        } catch (Exception e) {
            mutateSeedingPresenter.prepareFailView("Something went wrong with the API call, try again.");
        }
    }

    @Override
    public void switchToMainView() {
        mutateSeedingPresenter.switchToMainMenuView();
    }

}
