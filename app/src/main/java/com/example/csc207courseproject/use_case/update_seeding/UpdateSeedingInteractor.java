package com.example.csc207courseproject.use_case.update_seeding;

public class UpdateSeedingInteractor implements UpdateSeedingInputBoundary {
    private final UpdateSeedingOutputBoundary updateSeedingPresenter;

    public UpdateSeedingInteractor(UpdateSeedingOutputBoundary updateSeedingPresenter) {
        this.updateSeedingPresenter = updateSeedingPresenter;
    }

    @Override
    public void execute(UpdateSeedingInputData updateSeedingInputData) {
        int oldSeed = updateSeedingInputData.getOldSeed();
        int newSeed = updateSeedingInputData.getNewSeed();
        int maxSeed = updateSeedingInputData.getMaxSeed();

        if (oldSeed <= 0 || newSeed <= 0 || oldSeed > maxSeed || newSeed > maxSeed) {
            updateSeedingPresenter.prepareFailView("These are not valid seeds.");
        } else {
            UpdateSeedingOutputData s = new UpdateSeedingOutputData(oldSeed, newSeed);
            updateSeedingPresenter.prepareSuccessView(s);
        }
    }
}
