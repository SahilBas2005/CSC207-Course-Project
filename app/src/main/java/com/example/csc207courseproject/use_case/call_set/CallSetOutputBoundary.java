package com.example.csc207courseproject.use_case.call_set;


public interface CallSetOutputBoundary {

    /**
     * Prepares the success view for the call set Use Case.
     * @param outputData the output data
     */
    void prepareSuccessView(CallSetOutputData outputData);

    /**
     * Prepares the failure view for the call set use Case.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);

}
