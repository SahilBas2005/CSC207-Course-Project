package com.example.csc207courseprojectandroid.interface_adapter.select_phase;

import androidx.core.os.BuildCompat;
import com.example.csc207courseprojectandroid.interface_adapter.ViewManagerModel;
import com.example.csc207courseprojectandroid.interface_adapter.update_seeding.SeedingState;
import com.example.csc207courseprojectandroid.ui.seeding.SeedingViewModel;
import com.example.csc207courseprojectandroid.use_case.select_phase.SelectPhaseOutputBoundary;
import com.example.csc207courseprojectandroid.use_case.select_phase.SelectPhaseOutputData;

import java.util.List;

public class SelectPhasePresenter implements SelectPhaseOutputBoundary {

    private final SeedingViewModel seedingViewModel;
    private final ViewManagerModel viewManagerModel;

    public SelectPhasePresenter(SeedingViewModel seedingViewModel, ViewManagerModel viewManagerModel) {
        this.seedingViewModel = seedingViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void prepareSuccessView(SelectPhaseOutputData outputData) {
        final SeedingState seedingState = seedingViewModel.getState();
        final List<Integer> seeding = outputData.getSeeding();
        seedingState.setSeeding(seeding);
        seedingViewModel.firePropertyChanged("seedsuccess");
    }

    @Override
    public void prepareFailView(String errorMessage) {
        final SeedingState seedingState = seedingViewModel.getState();
        seedingState.setError(errorMessage);
        seedingViewModel.firePropertyChanged("seedfail");
    }
}