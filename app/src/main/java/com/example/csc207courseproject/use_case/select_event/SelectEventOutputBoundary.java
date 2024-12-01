package com.example.csc207courseproject.use_case.select_event;

/**
 * The output boundary for the Select Event Use Case.
 */
public interface SelectEventOutputBoundary {

    /**
     * Prepares the success view for the Select Event Use Case.
     */
    void prepareSuccessView();

    /**
     * Prepares the failure view for the Select Event Use Case.
     */
    void prepareFailView();
}
