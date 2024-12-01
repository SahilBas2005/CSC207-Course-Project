package com.example.csc207courseproject.use_case.select_event;

/**
 * The output boundary for the Select Event Use Case.
 */
public interface SelectEventOutputBoundary {

    /**
     * Prepares a view for when the use case is executed successfully.
     */
    void prepareSuccessView();

    /**
     * Prepares a view for when the use case fails.
     */
    void prepareFailView();
}
