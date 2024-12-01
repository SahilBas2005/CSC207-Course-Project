package com.example.csc207courseproject.interface_adapter.select_tournament;

import com.example.csc207courseproject.interface_adapter.select_event.EventState;
import com.example.csc207courseproject.interface_adapter.select_event.SelectEventViewModel;
import com.example.csc207courseproject.use_case.select_tournament.SelectTournamentOutputBoundary;
import com.example.csc207courseproject.use_case.select_tournament.SelectTournamentOutputData;

/**
 * Presenter for the Select Tournament Use Case.
 */
public class SelectTournamentPresenter implements SelectTournamentOutputBoundary {

    private final SelectTournamentViewModel selectTournamentViewModel;
    private final SelectEventViewModel selectEventViewModel;

    /**
     * The class constructor.
     * @param selectTournamentViewModel the view model to set for selectTournamentViewModel
     * @param selectEventViewModel the view model to set for selectEventViewModel
     */
    public SelectTournamentPresenter(SelectTournamentViewModel selectTournamentViewModel,
                                     SelectEventViewModel selectEventViewModel) {
        this.selectTournamentViewModel = selectTournamentViewModel;
        this.selectEventViewModel = selectEventViewModel;
    }

    /**
     * Prepares a view for when the use case is executed successfully.
     * @param selectTournamentOutputData necessary data to prepare the view
     */
    @Override
    public void prepareSuccessView(SelectTournamentOutputData selectTournamentOutputData) {
        final EventState eventState = selectEventViewModel.getState();
        eventState.setTournamentId(selectTournamentOutputData.getTournamentId());
        eventState.setEventNames(selectTournamentOutputData.getEventNames());
        eventState.setEventIds(selectTournamentOutputData.getEventIds());
        selectTournamentViewModel.firePropertyChanged("tournamentsuccess");
    }

    /**
     * Prepares a view for when the use case fails.
     */
    @Override
    public void prepareFailView() {
        selectTournamentViewModel.firePropertyChanged("tournamentfail");
    }
}
