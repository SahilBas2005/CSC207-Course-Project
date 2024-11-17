package interface_adapter.mutate_seeding;

import interface_adapter.ViewManagerModel;
import interface_adapter.main.MainViewModel;
import interface_adapter.update_seeding.SeedingState;
import interface_adapter.update_seeding.SeedingViewModel;
import use_case.mutate_seeding.MutateSeedingOutputBoundary;

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
