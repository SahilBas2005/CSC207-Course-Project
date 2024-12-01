package com.example.csc207courseproject.use_case.upcoming_sets;

import com.example.csc207courseproject.entities.Entrant;
import com.example.csc207courseproject.entities.EventData;
import com.example.csc207courseproject.entities.SetData;

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
        int eventId = EventData.getEventId();
        try {
            List<SetData> upcomingSets = dataAccess.getUpcomingSets(eventId);

            //Reopen free stations
            for (SetData upcomingSet : upcomingSets) {
                for(Entrant entrant : upcomingSet.getPlayers()) {
                    if (entrant.getCurrentStation() != null) {
                        entrant.getCurrentStation().setOccupied(false);
                    }
                }
            }

            UpcomingSetsOutputData s = new UpcomingSetsOutputData(dataAccess.getUpcomingSets(eventId));

            upcomingSetsPresenter.prepareSuccessView(s);
        } catch (Exception e) {
            upcomingSetsPresenter.prepareFailView();
        }
    }

}
