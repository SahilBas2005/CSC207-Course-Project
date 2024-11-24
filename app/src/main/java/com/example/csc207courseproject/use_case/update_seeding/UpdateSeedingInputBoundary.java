package com.example.csc207courseproject.use_case.update_seeding;
/**
 * Input Boundary for actions which are related to updating seeding locally for the display.
 */

public interface UpdateSeedingInputBoundary {
    /**
     * Executes the update seeding to Start.gg case.
     * @param updateSeedingInputData the input data
     */
    void execute(UpdateSeedingInputData updateSeedingInputData);

}
