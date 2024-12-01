package com.example.csc207courseproject.use_case.ongoing_sets;


public interface OngoingSetsOutputBoundary {

    /**
     * Prepares the success view for the get ongoing sets Use Case.
     * @param outputData the output data
     */
    void prepareSuccessView(OngoingSetsOutputData outputData);

    /**
     * Prepares the failure view for the get ongoing sets use Case.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);

}
