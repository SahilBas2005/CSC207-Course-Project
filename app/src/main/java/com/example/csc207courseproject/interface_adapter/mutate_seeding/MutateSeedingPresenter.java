package com.example.csc207courseproject.interface_adapter.mutate_seeding;

import com.example.csc207courseproject.interface_adapter.ViewManagerModel;
import com.example.csc207courseproject.interface_adapter.main.MainViewModel;
import com.example.csc207courseproject.interface_adapter.update_seeding.SeedingState;
import com.example.csc207courseproject.ui.seeding.SeedingViewModel;
import com.example.csc207courseproject.use_case.mutate_seeding.MutateSeedingOutputBoundary;

public class MutateSeedingPresenter implements MutateSeedingOutputBoundary {

    private final MainViewModel mainViewModel;
    private final SeedingViewModel seedingViewModel;
    private final ViewManagerModel viewManagerModel;

    public MutateSeedingPresenter(MainViewModel mainViewModel, SeedingViewModel seedingViewModel,
                                  ViewManagerModel viewManagerModel) {
        this.mainViewModel = mainViewModel;
        this.seedingViewModel = seedingViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void prepareSuccessView() {
        //viewManagerModel.setState(mainViewModel.getViewName());
        seedingViewModel.firePropertyChanged("mutatesuccess");
    }

    @Override
    public void prepareFailView(String errorMessage) {
        final SeedingState seedingState = seedingViewModel.getState();
        seedingState.setError(errorMessage);
        seedingViewModel.firePropertyChanged("mutatefail");
    }

    @Override
    public void switchToMainMenuView() {
        viewManagerModel.setState(mainViewModel.getViewName());
        viewManagerModel.firePropertyChanged();
    }
}
