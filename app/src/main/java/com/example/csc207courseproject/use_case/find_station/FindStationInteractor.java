package com.example.csc207courseproject.use_case.find_station;


import com.example.csc207courseproject.entities.Station;

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
        // Try to find an available station
        for (Station station : inputData.getStations()) {
            if (station.isNotOccupied() && !station.isStream()
                    && !inputData.getCurrentSet().conflictsWithStation(station)) {
                FindStationOutputData outputData = new FindStationOutputData(station);
                presenter.prepareSuccessView(outputData);
                return;
            }
        }
        presenter.prepareFailView();

    }

}
