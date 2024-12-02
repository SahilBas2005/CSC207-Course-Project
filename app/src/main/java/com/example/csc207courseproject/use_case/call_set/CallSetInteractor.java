package com.example.csc207courseproject.use_case.call_set;


import com.example.csc207courseproject.data_access.DataAccessException;
import com.example.csc207courseproject.entities.Station;

public class CallSetInteractor implements CallSetInputBoundary {

    private final CallSetOutputBoundary presenter;
    private final CallSetDataAccessInterface dataAccess;

    public CallSetInteractor(CallSetDataAccessInterface dataAccess,
                             CallSetOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    /**
     * Execute the logic for the call set use case.
     * @param inputData The input data
     */
    @Override
    public void execute(CallSetInputData inputData) {
        // Check if API call is successful
        try {
            Station station = inputData.getStation();

            // Mark station as occupied
            station.setOccupied(true);

            dataAccess.callSet(inputData.getSetId());
            CallSetOutputData s = new CallSetOutputData(station);

            presenter.prepareSuccessView(s);
        } catch (DataAccessException e) {
            presenter.prepareFailView();
        }
    }

}
