package com.example.csc207courseproject.use_case.select_tournament;

import java.util.List;

import org.json.JSONException;

import com.example.csc207courseproject.use_case.select_event.SelectEventDataAccessInterface;

/**
 * Interactor for the Select Tournament Use Case.
 */
public class SelectTournamentInteractor implements SelectTournamentInputBoundary {
    private final SelectTournamentOutputBoundary selectTournamentPresenter;
    private final SelectEventDataAccessInterface selectEventDataAccessObject;

    /**
     * The class constructor.
     * @param selectTournamentPresenter the presenter to set for selectTournamentPresenter
     * @param selectEventDataAccessObject the DAO to set for selectEventDataAccessObject
     */
    public SelectTournamentInteractor(SelectTournamentOutputBoundary selectTournamentPresenter,
                                      SelectEventDataAccessInterface selectEventDataAccessObject) {
        this.selectTournamentPresenter = selectTournamentPresenter;
        this.selectEventDataAccessObject = selectEventDataAccessObject;
    }

    /**
     * Executes the Select Tournament Use Case.
     * @param selectedTournamentId id of the tournament
     */
    @Override
    public void execute(Integer selectedTournamentId) {
        try {
            final List<List> events = selectEventDataAccessObject.getEventsInTournament(selectedTournamentId);
            final SelectTournamentOutputData selectTournamentOutputData =
                    new SelectTournamentOutputData(selectedTournamentId, events);
            selectTournamentPresenter.prepareSuccessView(selectTournamentOutputData);
        }
        catch (JSONException evt) {
            selectTournamentPresenter.prepareFailView();
        }
    }
}
