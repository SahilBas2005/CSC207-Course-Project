package com.example.csc207courseproject.interface_adapter.mutate_seeding;

import com.example.csc207courseproject.interface_adapter.update_seeding.SeedingState;
import com.example.csc207courseproject.ui.seeding.SeedingViewModel;
import com.example.csc207courseproject.use_case.mutate_seeding.MutateSeedingOutputBoundary;

public class MutateSeedingPresenter implements MutateSeedingOutputBoundary {

    private final SeedingViewModel seedingViewModel;

    public MutateSeedingPresenter(SeedingViewModel seedingViewModel) {
        this.seedingViewModel = seedingViewModel;
    }

    @Override
    public void prepareSuccessView() {
        seedingViewModel.firePropertyChanged("mutatesuccess");
    }

    @Override
    public void prepareFailView() {
        seedingViewModel.firePropertyChanged("mutatefail");
    }
}
