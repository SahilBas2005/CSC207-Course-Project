package com.example.csc207courseproject.interface_adapter.update_seeding;

import com.example.csc207courseproject.entities.Entrant;
import com.example.csc207courseproject.use_case.update_seeding.UpdateSeedingOutputBoundary;
import com.example.csc207courseproject.use_case.update_seeding.UpdateSeedingOutputData;
import com.example.csc207courseproject.ui.seeding.SeedingViewModel;

public class UpdateSeedingPresenter implements UpdateSeedingOutputBoundary {

    private final SeedingViewModel seedingViewModel;

    public UpdateSeedingPresenter(SeedingViewModel seedingViewModel) {
        this.seedingViewModel = seedingViewModel;
    }

    @Override
    public void prepareSuccessView(UpdateSeedingOutputData outputData) {
        final SeedingState seedingState = seedingViewModel.getState();
        final int oldSeed = outputData.getOldSeed();
        final int newSeed = outputData.getNewSeed();

        // Move seeds
        Entrant tempEntrant = seedingState.getSeeding().get(oldSeed - 1);
        seedingState.getSeeding().remove(oldSeed - 1);
        seedingState.getSeeding().add(newSeed - 1, tempEntrant);

        seedingViewModel.firePropertyChanged("updatesuccess");
    }

    @Override
    public void prepareFailView() {
        seedingViewModel.firePropertyChanged("updatefail");
    }
}
