package com.example.csc207courseproject.interface_adapter.tournament_description;

import com.example.csc207courseproject.use_case.tournament_description.TournamentDescriptionInputBoundary;

public class TournamentDescriptionController {

    private final TournamentDescriptionInputBoundary tournamentDescriptionUsecaseInteractor;

    public TournamentDescriptionController(TournamentDescriptionInputBoundary tournamentDescriptionUsecaseInteractor) {
        this.tournamentDescriptionUsecaseInteractor = tournamentDescriptionUsecaseInteractor;
    }

    public void execute() {
        tournamentDescriptionUsecaseInteractor.execute();
    }
}
