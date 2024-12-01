package com.example.csc207courseproject;//package com.example.csc207courseprojectandroid.app;

import com.example.csc207courseproject.data_access.APIDataAccessObject;
import com.example.csc207courseproject.data_access.OAuthOAuthDataAccessObject;
import com.example.csc207courseproject.interface_adapter.ViewManagerModel;
import com.example.csc207courseproject.interface_adapter.login.LoginController;
import com.example.csc207courseproject.interface_adapter.login.LoginPresenter;
import com.example.csc207courseproject.interface_adapter.login.LoginViewModel;
import com.example.csc207courseproject.interface_adapter.main.MainViewModel;
import com.example.csc207courseproject.interface_adapter.mutate_seeding.MutateSeedingController;
import com.example.csc207courseproject.interface_adapter.mutate_seeding.MutateSeedingPresenter;
import com.example.csc207courseproject.interface_adapter.select_event.SelectEventController;
import com.example.csc207courseproject.interface_adapter.select_event.SelectEventPresenter;
import com.example.csc207courseproject.interface_adapter.select_event.SelectEventViewModel;
import com.example.csc207courseproject.interface_adapter.select_phase.SelectPhaseController;
import com.example.csc207courseproject.interface_adapter.select_phase.SelectPhasePresenter;
import com.example.csc207courseproject.interface_adapter.select_tournament.SelectTournamentController;
import com.example.csc207courseproject.interface_adapter.select_tournament.SelectTournamentPresenter;
import com.example.csc207courseproject.interface_adapter.select_tournament.SelectTournamentViewModel;
import com.example.csc207courseproject.ui.call.CallViewModel;
import com.example.csc207courseproject.ui.seeding.SeedingViewModel;
import com.example.csc207courseproject.interface_adapter.update_seeding.UpdateSeedingController;
import com.example.csc207courseproject.interface_adapter.update_seeding.UpdateSeedingPresenter;
import com.example.csc207courseproject.ui.seeding.SeedingFragment;
import com.example.csc207courseproject.use_case.login.LoginInputBoundary;
import com.example.csc207courseproject.use_case.login.LoginInteractor;
import com.example.csc207courseproject.use_case.login.LoginOutputBoundary;
import com.example.csc207courseproject.ui.call.CallFragment;
import com.example.csc207courseproject.use_case.mutate_seeding.MutateSeedingInputBoundary;
import com.example.csc207courseproject.use_case.mutate_seeding.MutateSeedingInteractor;
import com.example.csc207courseproject.use_case.mutate_seeding.MutateSeedingOutputBoundary;
import com.example.csc207courseproject.use_case.select_event.SelectEventInputBoundary;
import com.example.csc207courseproject.use_case.select_event.SelectEventInteractor;
import com.example.csc207courseproject.use_case.select_event.SelectEventOutputBoundary;
import com.example.csc207courseproject.use_case.select_phase.SelectPhaseInputBoundary;
import com.example.csc207courseproject.use_case.select_phase.SelectPhaseInteractor;
import com.example.csc207courseproject.use_case.select_phase.SelectPhaseOutputBoundary;
import com.example.csc207courseproject.use_case.select_tournament.SelectTournamentInputBoundary;
import com.example.csc207courseproject.use_case.select_tournament.SelectTournamentInteractor;
import com.example.csc207courseproject.use_case.select_tournament.SelectTournamentOutputBoundary;
import com.example.csc207courseproject.use_case.update_seeding.UpdateSeedingInputBoundary;
import com.example.csc207courseproject.use_case.update_seeding.UpdateSeedingInteractor;
import com.example.csc207courseproject.use_case.update_seeding.UpdateSeedingOutputBoundary;
import com.example.csc207courseproject.view.ViewManager;

public class MainBuilder {
    private final ViewManagerModel viewManagerModel = new ViewManagerModel();

    private final APIDataAccessObject apiDataAccessObject = new APIDataAccessObject();
    private final OAuthOAuthDataAccessObject oAuthDataAccessObject = new OAuthOAuthDataAccessObject();

    private LoginViewModel loginViewModel;
    private SelectTournamentViewModel selectTournamentViewModel;
    private SelectEventViewModel selectEventViewModel;
    private SeedingViewModel seedingViewModel;
    private MainViewModel mainViewModel;
    private CallViewModel callViewModel;

    /**
     * Adds the Login View to the application.
     * @return this builder
     */
    public MainBuilder addLoginView() {
        loginViewModel = new LoginViewModel();
        return this;
    }

    /**
     * Adds the Select Tournament View to the application.
     * @return this builder
     */
    public MainBuilder addTournamentView() {
        selectTournamentViewModel = new SelectTournamentViewModel();
        return this;
    }

    /**
     * Adds the Select Event View to the application.
     * @return this builder
     */
    public MainBuilder addEventView() {
        selectEventViewModel = new SelectEventViewModel();
        return this;
    }

    /**
     * Adds the Seeding View to the application.
     * @return this builder
     */
    public MainBuilder addSeedingView() {
        seedingViewModel = new SeedingViewModel();
        SeedingFragment.setSeedingViewModel(seedingViewModel);
        return this;
    }

    /**
     * Adds the Call Set View to the application.
     * @return this builder
     */
    public MainBuilder addCallView() {

        // Set seeding view args
        callViewModel = new CallViewModel();
        CallFragment.setCallViewModel(callViewModel);
        return this;
    }

    /**
     * Adds the Main View to the application. INCOMPLETE
     * @return this builder
     */
    public MainBuilder addMainView() {

        mainViewModel = new MainViewModel();
        return this;
    }

    /**
     * Adds the login use case to the application.
     * @return this builder
     */
    public MainBuilder addLoginUseCase() {
        final LoginOutputBoundary loginPresenter = new LoginPresenter(
                loginViewModel, selectTournamentViewModel);
        final LoginInputBoundary loginInteractor = new LoginInteractor(
                oAuthDataAccessObject, loginPresenter, apiDataAccessObject);
        final LoginController controller = new LoginController(loginInteractor);
        LoginActivity.setLoginController(controller);
        LoginActivity.setLoginViewModel(loginViewModel);
        return this;
    }

    /**
     * Adds the select tournament use case to the application.
     * @return this builder
     */
    public MainBuilder addSelectTournamentUseCase() {
        final SelectTournamentOutputBoundary selectTournamentPresenter = new SelectTournamentPresenter(
                selectTournamentViewModel, selectEventViewModel);
        final SelectTournamentInputBoundary selectTournamentInteractor = new SelectTournamentInteractor(
                selectTournamentPresenter, apiDataAccessObject);
        final SelectTournamentController controller = new SelectTournamentController(selectTournamentInteractor);
        SelectTournamentActivity.setSelectTournamentController(controller);
        SelectTournamentActivity.setSelectTournamentViewModel(selectTournamentViewModel);
        return this;
    }

    /**
     * Adds the select event use case to the application.
     * @return this builder
     */
    public MainBuilder addSelectEventUseCase() {
        final SelectEventOutputBoundary selectEventPresenter = new SelectEventPresenter(selectEventViewModel);
        final SelectEventInputBoundary selectEventInteractor = new SelectEventInteractor(
                apiDataAccessObject, selectEventPresenter);
        final SelectEventController controller = new SelectEventController(selectEventInteractor);
        SelectEventActivity.setSelectEventController(controller);
        SelectEventActivity.setSelectEventViewModel(selectEventViewModel);
        return this;
    }

    /**
     * Adds the phase select Use Case to the application.
     * @return this builder
     */
    public MainBuilder addSelectPhaseUseCase() {
        final SelectPhaseOutputBoundary selectPhaseOutputBoundary = new SelectPhasePresenter(
                seedingViewModel, viewManagerModel);
        final SelectPhaseInputBoundary selectPhaseInteractor = new SelectPhaseInteractor(
                apiDataAccessObject, selectPhaseOutputBoundary);

        final SelectPhaseController controller = new SelectPhaseController(selectPhaseInteractor,
                seedingViewModel.getState());
        SeedingFragment.setSelectPhaseController(controller);
        return this;
    }

    /**
     * Adds the update seeding Use Case to the application.
     * @return this builder
     */
    public MainBuilder addUpdateSeedingUseCase() {
        final UpdateSeedingOutputBoundary updateSeedingOutputBoundary = new UpdateSeedingPresenter(
                seedingViewModel, viewManagerModel);
        final UpdateSeedingInputBoundary updateSeedingInteractor = new UpdateSeedingInteractor(
                updateSeedingOutputBoundary);

        final UpdateSeedingController controller = new UpdateSeedingController(updateSeedingInteractor,
                seedingViewModel.getState());
        SeedingFragment.setUpdateSeedingController(controller);
        return this;
    }

    /**
     * Adds the mutate seeding Use Case to the application.
     * @return this builder
     */
    public MainBuilder addMutateSeedingUseCase() {
        final MutateSeedingOutputBoundary mutateSeedingOutputBoundary = new MutateSeedingPresenter(
                mainViewModel, seedingViewModel, viewManagerModel);
        final MutateSeedingInputBoundary mutateSeedingInteractor = new MutateSeedingInteractor(
                apiDataAccessObject, mutateSeedingOutputBoundary);

        final MutateSeedingController controller = new MutateSeedingController(mutateSeedingInteractor,
                seedingViewModel.getState());
        SeedingFragment.setMutateSeedingController(controller);
        return this;
    }
}
