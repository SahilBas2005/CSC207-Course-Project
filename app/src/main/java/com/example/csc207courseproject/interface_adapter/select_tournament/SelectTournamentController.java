package com.example.csc207courseproject.interface_adapter.select_tournament;

import com.example.csc207courseproject.use_case.select_tournament.SelectTournamentInputBoundary;

/**
 * Controller for the Select Tournament Use Case.
 */
public class SelectTournamentController {

    private final SelectTournamentInputBoundary selectTournamentInteractor;

    /**
     * The class constructor.
     * @param selectTournamentInteractor the interactor to set for selectTournamentInteractor
     */
    public SelectTournamentController(SelectTournamentInputBoundary selectTournamentInteractor) {
        this.selectTournamentInteractor = selectTournamentInteractor;
    }

    /**
     * Executes the Select Tournament Use Case.
     * @param selectedTournamentId id of the tournament selected by the user
     */
    public void execute(Integer selectedTournamentId) {
        selectTournamentInteractor.execute(selectedTournamentId);
    }
}
