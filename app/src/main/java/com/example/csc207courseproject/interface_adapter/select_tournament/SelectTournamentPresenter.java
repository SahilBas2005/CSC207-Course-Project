package com.example.csc207courseproject.interface_adapter.select_tournament;

import com.example.csc207courseproject.interface_adapter.select_event.EventState;
import com.example.csc207courseproject.interface_adapter.select_event.SelectEventViewModel;
import com.example.csc207courseproject.use_case.select_tournament.SelectTournamentOutputBoundary;
import com.example.csc207courseproject.use_case.select_tournament.SelectTournamentOutputData;

public class SelectTournamentPresenter implements SelectTournamentOutputBoundary {

    private final SelectTournamentViewModel selectTournamentViewModel;
    private final SelectEventViewModel selectEventViewModel;

    public SelectTournamentPresenter(SelectTournamentViewModel selectTournamentViewModel,
                                     SelectEventViewModel selectEventViewModel) {
        this.selectTournamentViewModel = selectTournamentViewModel;
        this.selectEventViewModel = selectEventViewModel;
    }

    @Override
    public void prepareSuccessView(SelectTournamentOutputData selectTournamentOutputData) {
        final EventState eventState = selectEventViewModel.getState();
        eventState.setTournamentId(selectTournamentOutputData.getTournamentId());
        eventState.setEventNames(selectTournamentOutputData.getEventNames());
        eventState.setEventIds(selectTournamentOutputData.getEventIds());
        selectTournamentViewModel.firePropertyChanged("tournamentsuccess");
    }

    @Override
    public void prepareFailView() {
        selectTournamentViewModel.firePropertyChanged("tournamentfail");
    }
}
