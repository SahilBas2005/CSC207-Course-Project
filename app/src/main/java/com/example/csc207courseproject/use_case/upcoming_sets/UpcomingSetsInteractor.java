package com.example.csc207courseproject.use_case.upcoming_sets;

import com.example.csc207courseproject.data_access.api.APIDataAccessException;
import com.example.csc207courseproject.entities.CallSetData;
import com.example.csc207courseproject.entities.Entrant;
import com.example.csc207courseproject.entities.EventData;

import java.util.List;

public class UpcomingSetsInteractor implements UpcomingSetsInputBoundary {

    private final UpcomingSetsOutputBoundary upcomingSetsPresenter;
    private final UpcomingSetsDataAccessInterface dataAccess;

    public UpcomingSetsInteractor(UpcomingSetsDataAccessInterface dataAccess,
                                  UpcomingSetsOutputBoundary upcomingSetsPresenter) {
        this.dataAccess = dataAccess;
        this.upcomingSetsPresenter = upcomingSetsPresenter;
    }

    @Override
    public void execute() {
        // Check if API call is successful
        int eventId = EventData.getEventData().getEventId();
        try {
            List<CallSetData> upcomingSets = dataAccess.getUpcomingSets(eventId);

            //Reopen free stations
            for (CallSetData upcomingSet : upcomingSets) {
                for(Entrant entrant : upcomingSet.getPlayers()) {
                    if (entrant.getCurrentStation() != null) {
                        entrant.getCurrentStation().setOccupied(false);
                    }
                }
            }

            UpcomingSetsOutputData s = new UpcomingSetsOutputData(dataAccess.getUpcomingSets(eventId));

            upcomingSetsPresenter.prepareSuccessView(s);
        } catch (APIDataAccessException e) {
            upcomingSetsPresenter.prepareFailView();
        }
    }

}
