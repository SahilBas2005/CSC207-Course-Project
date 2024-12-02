package com.example.csc207courseproject.interface_adapter.tournament_description;

import com.example.csc207courseproject.use_case.tournament_description.TournamentDescriptionInputBoundary;
import com.example.csc207courseproject.use_case.tournament_description.TournamentDescriptionInputData;

public class TournamentDescriptionController {

    private final TournamentDescriptionInputBoundary tournamentDescriptionUsecaseInteractor;

    public TournamentDescriptionController(TournamentDescriptionInputBoundary tournamentDescriptionUsecaseInteractor) {
        this.tournamentDescriptionUsecaseInteractor = tournamentDescriptionUsecaseInteractor;
    }

    public void execute() {
        TournamentDescriptionInputData tournamentDescriptionInputData = new TournamentDescriptionInputData();
        tournamentDescriptionUsecaseInteractor.execute(tournamentDescriptionInputData);
    }
}
