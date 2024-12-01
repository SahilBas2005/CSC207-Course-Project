package com.example.csc207courseproject.use_case.select_tournament;

import java.util.List;

import org.json.JSONException;

/**
 * Interactor for the Select Tournament Use Case.
 */
public class SelectTournamentInteractor implements SelectTournamentInputBoundary {
    private final SelectTournamentOutputBoundary selectTournamentPresenter;
    private final SelectTournamentDataAccessInterface selectTournamentDataAccessInterface;

    /**
     * The class constructor.
     * @param selectTournamentPresenter the presenter to set for selectTournamentPresenter
     * @param selectTournamentDataAccessInterface the DAO to set for selectTournamentDataAccessInterface
     */
    public SelectTournamentInteractor(SelectTournamentOutputBoundary selectTournamentPresenter,
                                      SelectTournamentDataAccessInterface selectTournamentDataAccessInterface) {
        this.selectTournamentPresenter = selectTournamentPresenter;
        this.selectTournamentDataAccessInterface = selectTournamentDataAccessInterface;
    }

    /**
     * Executes the Select Tournament Use Case.
     * @param selectedTournamentId id of the tournament
     */
    @Override
    public void execute(Integer selectedTournamentId) {
        try {
            final List<List> events = selectTournamentDataAccessInterface.getEventsInTournament(selectedTournamentId);
            final SelectTournamentOutputData selectTournamentOutputData =
                    new SelectTournamentOutputData(selectedTournamentId, events);
            selectTournamentPresenter.prepareSuccessView(selectTournamentOutputData);
        }
        catch (JSONException evt) {
            selectTournamentPresenter.prepareFailView();
        }
    }
}
