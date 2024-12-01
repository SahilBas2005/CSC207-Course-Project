package com.example.csc207courseproject.use_case.upcoming_sets;


public interface UpcomingSetsOutputBoundary {

    /**
     * Prepares the success view for the Update Seeding Use Case.
     * @param outputData the output data
     */
    void prepareSuccessView(UpcomingSetsOutputData outputData);

    /**
     * Prepares the failure view for the Update Seeding Use Case.
     */
    void prepareFailView();

}
