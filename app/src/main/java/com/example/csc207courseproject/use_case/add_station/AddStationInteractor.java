package com.example.csc207courseproject.use_case.add_station;


import android.util.Log;
import com.example.csc207courseproject.entities.EventData;
import com.example.csc207courseproject.entities.Station;

import java.util.List;

public class AddStationInteractor implements AddStationInputBoundary {

    private final AddStationOutputBoundary presenter;
    private final AddStationDataAccessInterface dataAccess;

    public AddStationInteractor(AddStationDataAccessInterface dataAccess,
                                AddStationOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(AddStationInputData inputData) {
        // Check if API call is successful
        int tournamentId = EventData.getTournamentId();
        try {

            int stationId = dataAccess.addStation(tournamentId, inputData.getStationNumber());

            Station station = new Station(stationId, inputData.getStationNumber());
            AddStationOutputData s = new AddStationOutputData(station);

            presenter.prepareSuccessView(s);
        } catch (Exception e) {
            presenter.prepareFailView("Something went wrong with the API call, try again.");
        }
    }

}
