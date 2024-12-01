package com.example.csc207courseproject.use_case.select_tournament;

/**
 * The output boundary for the Select Tournament Use Case.
 */
public interface SelectTournamentOutputBoundary {

    /**
     * Prepares the success view for the Select Tournament Use Case.
     */
    void prepareSuccessView(SelectTournamentOutputData selectTournamentOutputData);

    /**
     * Prepares the failure view for the Select Tournament Use Case.
     */
    void prepareFailView();
}
