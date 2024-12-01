package com.example.csc207courseproject;//package com.example.csc207courseprojectandroid.app;

import com.example.csc207courseproject.data_access.api.APIDataAccessObject;
import com.example.csc207courseproject.data_access.OAuth.OAuthDataAccessObject;
import com.example.csc207courseproject.interface_adapter.add_station.AddStationController;
import com.example.csc207courseproject.interface_adapter.add_station.AddStationPresenter;
import com.example.csc207courseproject.interface_adapter.call_set.CallSetController;
import com.example.csc207courseproject.interface_adapter.call_set.CallSetPresenter;
import com.example.csc207courseproject.interface_adapter.find_station.FindStationController;
import com.example.csc207courseproject.interface_adapter.find_station.FindStationPresenter;
import com.example.csc207courseproject.interface_adapter.login.LoginController;
import com.example.csc207courseproject.interface_adapter.login.LoginPresenter;
import com.example.csc207courseproject.interface_adapter.get_stations.GetStationsController;
import com.example.csc207courseproject.interface_adapter.get_stations.GetStationsPresenter;
import com.example.csc207courseproject.interface_adapter.login.LoginViewModel;
import com.example.csc207courseproject.interface_adapter.main.MainViewModel;
import com.example.csc207courseproject.interface_adapter.mutate_seeding.MutateSeedingController;
import com.example.csc207courseproject.interface_adapter.mutate_seeding.MutateSeedingPresenter;
import com.example.csc207courseproject.interface_adapter.ongoing_sets.OngoingSetsController;
import com.example.csc207courseproject.interface_adapter.ongoing_sets.OngoingSetsPresenter;
import com.example.csc207courseproject.interface_adapter.report_game.ReportGameController;
import com.example.csc207courseproject.interface_adapter.report_game.ReportGamePresenter;
import com.example.csc207courseproject.interface_adapter.report_set.ReportSetController;
import com.example.csc207courseproject.interface_adapter.report_set.ReportSetPresenter;
import com.example.csc207courseproject.interface_adapter.select_event.SelectEventController;
import com.example.csc207courseproject.interface_adapter.select_event.SelectEventPresenter;
import com.example.csc207courseproject.interface_adapter.select_event.SelectEventViewModel;
import com.example.csc207courseproject.interface_adapter.select_phase.SelectPhaseController;
import com.example.csc207courseproject.interface_adapter.select_phase.SelectPhasePresenter;
import com.example.csc207courseproject.interface_adapter.select_tournament.SelectTournamentController;
import com.example.csc207courseproject.interface_adapter.select_tournament.SelectTournamentPresenter;
import com.example.csc207courseproject.interface_adapter.select_tournament.SelectTournamentViewModel;
import com.example.csc207courseproject.interface_adapter.upcoming_sets.UpcomingSetsController;
import com.example.csc207courseproject.interface_adapter.upcoming_sets.UpcomingSetsPresenter;
import com.example.csc207courseproject.ui.call.CallSetFragment;
import com.example.csc207courseproject.ui.call.CallStationFragment;
import com.example.csc207courseproject.ui.call.CallViewModel;
import com.example.csc207courseproject.ui.report.ReportFragment;
import com.example.csc207courseproject.ui.report.ReportSetFragment;
import com.example.csc207courseproject.ui.report.ReportViewModel;
import com.example.csc207courseproject.ui.seeding.SeedingViewModel;
import com.example.csc207courseproject.interface_adapter.update_seeding.UpdateSeedingController;
import com.example.csc207courseproject.interface_adapter.update_seeding.UpdateSeedingPresenter;
import com.example.csc207courseproject.ui.seeding.SeedingFragment;
import com.example.csc207courseproject.use_case.login.LoginInputBoundary;
import com.example.csc207courseproject.use_case.login.LoginInteractor;
import com.example.csc207courseproject.use_case.login.LoginOutputBoundary;
import com.example.csc207courseproject.ui.call.CallFragment;
import com.example.csc207courseproject.use_case.add_station.AddStationInputBoundary;
import com.example.csc207courseproject.use_case.add_station.AddStationInteractor;
import com.example.csc207courseproject.use_case.add_station.AddStationOutputBoundary;
import com.example.csc207courseproject.use_case.call_set.CallSetInputBoundary;
import com.example.csc207courseproject.use_case.call_set.CallSetInteractor;
import com.example.csc207courseproject.use_case.call_set.CallSetOutputBoundary;
import com.example.csc207courseproject.use_case.find_station.FindStationInputBoundary;
import com.example.csc207courseproject.use_case.find_station.FindStationInteractor;
import com.example.csc207courseproject.use_case.find_station.FindStationOutputBoundary;
import com.example.csc207courseproject.use_case.get_stations.GetStationsInputBoundary;
import com.example.csc207courseproject.use_case.get_stations.GetStationsInteractor;
import com.example.csc207courseproject.use_case.get_stations.GetStationsOutputBoundary;
import com.example.csc207courseproject.use_case.mutate_seeding.MutateSeedingInputBoundary;
import com.example.csc207courseproject.use_case.mutate_seeding.MutateSeedingInteractor;
import com.example.csc207courseproject.use_case.mutate_seeding.MutateSeedingOutputBoundary;
import com.example.csc207courseproject.use_case.report_game.ReportGameInputBoundary;
import com.example.csc207courseproject.use_case.report_game.ReportGameInteractor;
import com.example.csc207courseproject.use_case.report_game.ReportGameOutputBoundary;
import com.example.csc207courseproject.use_case.select_event.SelectEventInputBoundary;
import com.example.csc207courseproject.use_case.select_event.SelectEventInteractor;
import com.example.csc207courseproject.use_case.select_event.SelectEventOutputBoundary;
import com.example.csc207courseproject.use_case.ongoing_sets.OngoingSetsInputBoundary;
import com.example.csc207courseproject.use_case.ongoing_sets.OngoingSetsInteractor;
import com.example.csc207courseproject.use_case.ongoing_sets.OngoingSetsOutputBoundary;
import com.example.csc207courseproject.use_case.report_set.ReportSetInputBoundary;
import com.example.csc207courseproject.use_case.report_set.ReportSetInteractor;
import com.example.csc207courseproject.use_case.report_set.ReportSetOutputBoundary;
import com.example.csc207courseproject.use_case.select_phase.SelectPhaseInputBoundary;
import com.example.csc207courseproject.use_case.select_phase.SelectPhaseInteractor;
import com.example.csc207courseproject.use_case.select_phase.SelectPhaseOutputBoundary;
import com.example.csc207courseproject.use_case.select_tournament.SelectTournamentInputBoundary;
import com.example.csc207courseproject.use_case.select_tournament.SelectTournamentInteractor;
import com.example.csc207courseproject.use_case.select_tournament.SelectTournamentOutputBoundary;
import com.example.csc207courseproject.use_case.upcoming_sets.UpcomingSetsInputBoundary;
import com.example.csc207courseproject.use_case.upcoming_sets.UpcomingSetsInteractor;
import com.example.csc207courseproject.use_case.upcoming_sets.UpcomingSetsOutputBoundary;
import com.example.csc207courseproject.use_case.update_seeding.UpdateSeedingInputBoundary;
import com.example.csc207courseproject.use_case.update_seeding.UpdateSeedingInteractor;
import com.example.csc207courseproject.use_case.update_seeding.UpdateSeedingOutputBoundary;
import com.example.csc207courseproject.view.ViewManager;

public class MainBuilder {
    private final ViewManagerModel viewManagerModel = new ViewManagerModel();
    private final ViewManager viewManager = new ViewManager(viewManagerModel);

    private final APIDataAccessObject apiDataAccessObject = new APIDataAccessObject();
    private final OAuthDataAccessObject oAuthDataAccessObject = new OAuthDataAccessObject();

    private LoginViewModel loginViewModel;
    private SelectTournamentViewModel selectTournamentViewModel;
    private SelectEventViewModel selectEventViewModel;
    private SeedingViewModel seedingViewModel;
    private MainViewModel mainViewModel;
    private CallViewModel callViewModel;
    private ReportViewModel reportViewModel;

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
     * Adds the Report Set View to the application.
     * @return this builder
     */
    public MainBuilder addReportView() {

        // Set report view args
        reportViewModel = new ReportViewModel();
        ReportFragment.setReportViewModel(reportViewModel);
        return this;
    }

    /**
     * Adds the upcoming sets Use Case to the application.
     * @return this builder
     */
    public MainBuilder addUpcomingSetsUseCase() {
        final UpcomingSetsOutputBoundary upcomingSetsOutputBoundary = new UpcomingSetsPresenter(
                callViewModel, viewManagerModel);
        final UpcomingSetsInputBoundary upcomingSetsInteractor = new UpcomingSetsInteractor(
                apiDataAccessObject, upcomingSetsOutputBoundary);

        final UpcomingSetsController controller = new UpcomingSetsController(upcomingSetsInteractor);
        CallFragment.setUpcomingSetsController(controller);
        return this;
    }

    /**
     * Adds the get stations Use Case to the application.
     * @return this builder
     */
    public MainBuilder addGetStationsUseCase() {
        final GetStationsOutputBoundary outputBoundary = new GetStationsPresenter(
                callViewModel);
        final GetStationsInputBoundary interactor = new GetStationsInteractor(
                apiDataAccessObject, outputBoundary);

        final GetStationsController controller = new GetStationsController(interactor,
                callViewModel.getState());
        CallFragment.setGetStationsController(controller);
        return this;
    }

    /**
     * Adds the add station Use Case to the application.
     * @return this builder
     */
    public MainBuilder addAddStationUseCase() {
        final AddStationOutputBoundary outputBoundary = new AddStationPresenter(
                callViewModel);
        final AddStationInputBoundary interactor = new AddStationInteractor(
                apiDataAccessObject, outputBoundary);

        final AddStationController controller = new AddStationController(interactor,
                callViewModel.getState());
        CallStationFragment.setAddStationController(controller);
        return this;
    }

    /**
     * Adds the call set Use Case to the application.
     * @return this builder
     */
    public MainBuilder addCallSetUseCase() {
        final CallSetOutputBoundary outputBoundary = new CallSetPresenter(
                callViewModel);
        final CallSetInputBoundary interactor = new CallSetInteractor(
                apiDataAccessObject, outputBoundary);

        final CallSetController controller = new CallSetController(interactor,
                callViewModel.getState());
        CallSetFragment.setCallSetController(controller);
        return this;
    }

    /**
     * Adds the find station Use Case to the application.
     * @return this builder
     */
    public MainBuilder addFindStationUseCase() {
        final FindStationOutputBoundary outputBoundary = new FindStationPresenter(
                callViewModel);
        final FindStationInputBoundary interactor = new FindStationInteractor(outputBoundary);

        final FindStationController controller = new FindStationController(interactor,
                callViewModel.getState());
        CallFragment.setFindStationController(controller);
        return this;
    }

    /**
     * Adds the ongoing sets Use Case to the application.
     * @return this builder
     */
    public MainBuilder addOngoingSetsUseCase() {
        final OngoingSetsOutputBoundary ongoingSetsOutputBoundary = new OngoingSetsPresenter(
                reportViewModel, viewManagerModel);
        final OngoingSetsInputBoundary ongoingSetsInteractor = new OngoingSetsInteractor(
                apiDataAccessObject, ongoingSetsOutputBoundary);

        final OngoingSetsController controller = new OngoingSetsController(ongoingSetsInteractor,
                reportViewModel.getState());
        ReportFragment.setOngoingSetsController(controller);
        return this;
    }

    /**
     * Adds the Report Game Use Case to the application.
     * @return this builder
     */
    public MainBuilder addReportGameUseCase() {
        final ReportGameOutputBoundary reportGameOutputBoundary = new ReportGamePresenter(
                reportViewModel, viewManagerModel);
        final ReportGameInputBoundary reportGameInteractor = new ReportGameInteractor(
                reportGameOutputBoundary);

        final ReportGameController controller = new ReportGameController(reportGameInteractor,
                reportViewModel.getState());
        ReportSetFragment.setReportGameController(controller);
        return this;
    }

    /**
     * Adds the Report Set Use Case to the application.
     * @return this builder
     */
    public MainBuilder addReportSetUseCase() {
        //Figure out why this takes in two things, then do the api testing
        final ReportSetOutputBoundary reportSetOutputBoundary = new ReportSetPresenter(
                reportViewModel, viewManagerModel);
        final ReportSetInputBoundary reportSetInteractor = new ReportSetInteractor(
                apiDataAccessObject, reportSetOutputBoundary);

        final ReportSetController controller = new ReportSetController(reportSetInteractor,
                reportViewModel.getState());
        ReportSetFragment.setReportSetController(controller);
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
                apiDataAccessObject, selectTournamentPresenter, apiDataAccessObject);
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
