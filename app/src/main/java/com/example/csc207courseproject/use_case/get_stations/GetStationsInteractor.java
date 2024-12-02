package com.example.csc207courseproject.use_case.get_stations;

import com.example.csc207courseproject.data_access.DataAccessException;
import com.example.csc207courseproject.entities.EventData;
import com.example.csc207courseproject.entities.Station;

import java.util.List;

public class GetStationsInteractor implements GetStationsInputBoundary {

    private final GetStationsOutputBoundary presenter;
    private final GetStationsDataAccessInterface dataAccess;

    public GetStationsInteractor(GetStationsDataAccessInterface dataAccess,
                                 GetStationsOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(GetStationsInputData inputData) {
        // Check if API call is successful
        int eventId = EventData.getEventData().getEventId();
        try {
            // Combine api stations with local stations
            List<Station> stations = dataAccess.getStations(eventId);
            stations.addAll(inputData.getlocalStations());

            GetStationsOutputData s = new GetStationsOutputData(stations);

            presenter.prepareSuccessView(s);
        } catch (DataAccessException e) {
            presenter.prepareFailView();
        }
    }

}
