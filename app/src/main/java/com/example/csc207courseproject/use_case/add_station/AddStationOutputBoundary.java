package com.example.csc207courseproject.use_case.add_station;


public interface AddStationOutputBoundary {

    /**
     * Prepares the success view for the add station Use Case.
     * @param outputData the output data
     */
    void prepareSuccessView(AddStationOutputData outputData);

    /**
     * Prepares the failure view for the add station use Case.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);

}
