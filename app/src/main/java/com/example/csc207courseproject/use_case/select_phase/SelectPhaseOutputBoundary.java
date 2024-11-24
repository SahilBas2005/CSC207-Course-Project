package com.example.csc207courseproject.use_case.select_phase;

/**
 * The output boundary for the Select Phase Use Case.
 */
public interface SelectPhaseOutputBoundary {

    /**
     * Prepares the success view for the Select Phase Use Case.
     * @param outputData the output data
     */
    void prepareSuccessView(SelectPhaseOutputData outputData);

    /**
     * Prepares the failure view for the Select Phase Use Case.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);
}
