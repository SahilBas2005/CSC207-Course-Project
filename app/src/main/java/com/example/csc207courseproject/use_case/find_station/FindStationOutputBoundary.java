package com.example.csc207courseproject.use_case.find_station;


public interface FindStationOutputBoundary {

    /**
     * Prepares the success view for the find station Use Case.
     * @param outputData the output data
     */
    void prepareSuccessView(FindStationOutputData outputData);

    /**
     * Prepares the failure view for the find station use Case.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);

}
