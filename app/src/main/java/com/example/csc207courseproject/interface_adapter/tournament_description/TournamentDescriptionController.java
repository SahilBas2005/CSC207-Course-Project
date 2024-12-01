package com.example.csc207courseproject.interface_adapter.tournament_description;

import com.example.csc207courseproject.use_case.tournament_description.TournamentDescriptionInputBoundary;
import com.example.csc207courseproject.use_case.tournament_description.TournamentDescriptionInputData;

public class TournamentDescriptionController {

    private final TournamentDescriptionInputBoundary tournamentDescriptionUsecaseInteractor;
//    private final TournamentState tournamentState;

    public TournamentDescriptionController(TournamentDescriptionInputBoundary tournamentDescriptionUsecaseInteractor) {
        this.tournamentDescriptionUsecaseInteractor = tournamentDescriptionUsecaseInteractor;
//        this.tournamentState = tournamentState;
    }

    public void execute() {

        Integer tournamentId = 1257516;
        final TournamentDescriptionInputData tournamentDescriptionInputData = new TournamentDescriptionInputData(tournamentId);
        tournamentDescriptionUsecaseInteractor.execute(tournamentDescriptionInputData);
    }
}
