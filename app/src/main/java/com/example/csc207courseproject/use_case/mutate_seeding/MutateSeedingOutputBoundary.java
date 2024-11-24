package com.example.csc207courseproject.use_case.mutate_seeding;

/**
 * The output boundary for the Mutate Seeding Use Case.
 */
public interface MutateSeedingOutputBoundary {

    /**
     * Prepares the success view for the Mutate Seeding Use Case.
     */
    void prepareSuccessView();

    /**
     * Prepares the failure view for the Mutate Seeding Use Case.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);

    /**
     * Switches to the Main Menu View.
     */
    void switchToMainMenuView();
}
