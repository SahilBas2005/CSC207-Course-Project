package com.example.csc207courseproject.use_case.update_seeding;

/**
 * The output boundary for the Update Seeding Use Case.
 */
public interface UpdateSeedingOutputBoundary {

    /**
     * Prepares the success view for the Update Seeding Use Case.
     * @param outputData the output data
     */
    void prepareSuccessView(UpdateSeedingOutputData outputData);

    /**
     * Prepares the failure view for the Update Seeding Use Case.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);
}
