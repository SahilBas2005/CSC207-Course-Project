package com.example.csc207courseproject.use_case.get_phases;


public interface GetPhasesOutputBoundary {

    /**
     * Prepares the success view for the find station Use Case.
     * @param outputData the output data
     */
    void prepareSuccessView(GetPhasesOutputData outputData);

    /**
     * Prepares the failure view for the find station use Case.
     */
    void prepareFailView();

}
