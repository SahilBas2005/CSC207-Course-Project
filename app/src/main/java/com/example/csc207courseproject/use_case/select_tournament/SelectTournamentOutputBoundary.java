package com.example.csc207courseproject.use_case.select_tournament;

/**
 * The output boundary for the Select Tournament Use Case.
 */
public interface SelectTournamentOutputBoundary {

    /**
     * Prepares a view for when the use case is executed successfully.
     * @param selectTournamentOutputData necessary data to prepare the view
     */
    void prepareSuccessView(SelectTournamentOutputData selectTournamentOutputData);

    /**
     * Prepares a view for when the use case fails.
     */
    void prepareFailView();
}
