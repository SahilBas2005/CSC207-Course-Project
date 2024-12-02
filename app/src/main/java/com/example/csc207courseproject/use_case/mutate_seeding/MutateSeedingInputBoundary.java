package com.example.csc207courseproject.use_case.mutate_seeding;
/**
 * Input Boundary for actions which are related to mutating seeding to Start.gg's website
 */
public interface MutateSeedingInputBoundary {
    /**
     * Executes the update seeding to Start.gg case.
     * @param mutateSeedingInputData the input data
     */
    void execute(MutateSeedingInputData mutateSeedingInputData);
}
