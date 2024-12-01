package com.example.csc207courseproject.use_case.tournament_description;

public interface TournamentDescriptionOutputBoundary {

    void prepareSuccessView(TournamentDescriptionOutputData outputData);

    void prepareFailView(String errorMessage);

}
