package com.example.csc207courseproject.interface_adapter.select_tournament;

import com.example.csc207courseproject.use_case.login.LoginInputBoundary;
import com.example.csc207courseproject.use_case.select_tournament.SelectTournamentInputBoundary;

/**
 * Controller for the Select Tournament Use Case.
 */
public class SelectTournamentController {

    private final SelectTournamentInputBoundary selectTournamentInteractor;

    public SelectTournamentController(SelectTournamentInputBoundary selectTournamentInteractor) {
        this.selectTournamentInteractor = selectTournamentInteractor;
    }

    public void execute(Integer selectedTournamentId) {
        selectTournamentInteractor.execute(selectedTournamentId);
    }
}
