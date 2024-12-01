package com.example.csc207courseproject.use_case.find_station;


import com.example.csc207courseproject.entities.Station;

import java.util.List;

public class FindStationInteractor implements FindStationInputBoundary {

    private final FindStationOutputBoundary presenter;

    public FindStationInteractor(FindStationOutputBoundary presenter) {
        this.presenter = presenter;
    }

    /**
     * Execute the logic for the find station use case.
     * @param inputData The input data
     */
    @Override
    public void execute(FindStationInputData inputData) {
        // Check if there is an openstream
        Station openStream = findStream(inputData.getStations());

        // Try to find an available station that doesn't conflict with the user's tags
        for (Station station : inputData.getStations()) {
            if (station.isNotOccupied() && !station.isStream()
                    && !inputData.getCurrentSet().conflictsWithStation(station)) {
                FindStationOutputData outputData = new FindStationOutputData(station, openStream);
                presenter.prepareSuccessView(outputData);
                return;
            }
        }

        // Prepare fail view if no stations are available
        presenter.prepareFailView();

    }

    /**
     * Finds and assigns returns a station with an open stream setup.
     * @return Returns an open stream station or null if none exist
     */
    private Station findStream(List<Station> stations) {
        for (Station station : stations) {
            if (station.isNotOccupied() && station.isStream()) {
                return station;
            }
        }
        return null;
    }

}
