package com.example.csc207courseproject.use_case.get_stations;


public interface GetStationsOutputBoundary {

    /**
     * Prepares the success view for the get stations Use Case.
     * @param outputData the output data
     */
    void prepareSuccessView(GetStationsOutputData outputData);

    /**
     * Prepares the failure view for the get stations use Case.
     */
    void prepareFailView();

}
