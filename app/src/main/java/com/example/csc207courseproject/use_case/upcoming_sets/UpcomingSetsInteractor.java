package com.example.csc207courseproject.use_case.upcoming_sets;

import com.example.csc207courseproject.data_access.DataAccessException;
import com.example.csc207courseproject.entities.CallSetData;
import com.example.csc207courseproject.entities.Entrant;
import com.example.csc207courseproject.entities.EventData;

import java.util.ArrayList;
import java.util.List;

public class UpcomingSetsInteractor implements UpcomingSetsInputBoundary {

    private final UpcomingSetsOutputBoundary upcomingSetsPresenter;
    private final UpcomingSetsDataAccessInterface dataAccess;

    public UpcomingSetsInteractor(UpcomingSetsDataAccessInterface dataAccess,
                                  UpcomingSetsOutputBoundary upcomingSetsPresenter) {
        this.dataAccess = dataAccess;
        this.upcomingSetsPresenter = upcomingSetsPresenter;
    }

    /**
     * Execute the upcoming sets use case
     * @param inputData The input data
     */
    @Override
    public void execute(UpcomingSetsInputData inputData) {
        // Check if API call is successful
        int eventId = EventData.getEventData().getEventId();
        try {
            List<CallSetData> upcomingSets = dataAccess.getUpcomingSets(eventId);

            // Find recently reported sets that might remain due to database mutation delays
            List<CallSetData> repeats = new ArrayList<>();

            for (CallSetData set : upcomingSets) {
                if (inputData.getcalledSetIds().contains(set.getSetID())) {
                    repeats.add(set);
                }
            }

            // Remove recently reported sets
            upcomingSets.removeAll(repeats);

            // Reopen free stations
            for (CallSetData upcomingSet : upcomingSets) {
                for(Entrant entrant : upcomingSet.getPlayers()) {
                    if (entrant.getCurrentStation() != null) {
                        entrant.getCurrentStation().setOccupied(false);
                    }
                }
            }

            // Prepare success view
            UpcomingSetsOutputData outputData = new UpcomingSetsOutputData(upcomingSets);
            upcomingSetsPresenter.prepareSuccessView(outputData);
        } catch (DataAccessException e) {
            // If the API call failed, prepare the fail view
            upcomingSetsPresenter.prepareFailView();
        }
    }
}
