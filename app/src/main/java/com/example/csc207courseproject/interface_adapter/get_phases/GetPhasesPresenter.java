package com.example.csc207courseproject.interface_adapter.get_phases;

import com.example.csc207courseproject.interface_adapter.update_seeding.SeedingState;
import com.example.csc207courseproject.ui.seeding.SeedingViewModel;
import com.example.csc207courseproject.use_case.get_phases.GetPhasesOutputBoundary;
import com.example.csc207courseproject.use_case.get_phases.GetPhasesOutputData;

public class GetPhasesPresenter implements GetPhasesOutputBoundary {

    private final SeedingViewModel viewModel;

    public GetPhasesPresenter(SeedingViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(GetPhasesOutputData outputData) {
        SeedingState currentState = viewModel.getState();
        currentState.setPhaseNames(outputData.getPhases());
        viewModel.firePropertyChanged("getphasessuccess");
    }

    @Override
    public void prepareFailView() {
        viewModel.firePropertyChanged("getphasesfail");
    }

}
