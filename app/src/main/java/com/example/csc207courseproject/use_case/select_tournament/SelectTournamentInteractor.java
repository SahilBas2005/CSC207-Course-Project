package com.example.csc207courseproject.use_case.select_tournament;

import com.example.csc207courseproject.use_case.select_event.SelectEventDataAccessInterface;
import com.example.csc207courseproject.use_case.select_event.SelectEventOutputData;
import org.json.JSONException;

import java.util.List;

public class SelectTournamentInteractor implements SelectTournamentInputBoundary {
    private final SelectTournamentDataAccessInterface selectTournamentDataAccessObject;
    private final SelectTournamentOutputBoundary selectTournamentPresenter;
    private final SelectEventDataAccessInterface selectEventDataAccessObject;

    public SelectTournamentInteractor(SelectTournamentDataAccessInterface selectTournamentDataAccessObject,
                                      SelectTournamentOutputBoundary selectTournamentPresenter,
                                      SelectEventDataAccessInterface selectEventDataAccessObject) {
        this.selectTournamentDataAccessObject = selectTournamentDataAccessObject;
        this.selectTournamentPresenter = selectTournamentPresenter;
        this.selectEventDataAccessObject = selectEventDataAccessObject;
    }

    @Override
    public void execute(Integer selectedTournamentId) {
        try {
            List<List> events = selectEventDataAccessObject.getEventsInTournament(selectedTournamentId);
            final SelectTournamentOutputData selectTournamentOutputData =
                    new SelectTournamentOutputData(selectedTournamentId, events);
            selectTournamentPresenter.prepareSuccessView(selectTournamentOutputData);
        } catch (JSONException e) {
            selectTournamentPresenter.prepareFailView();
        }
    }
}
