package com.example.csc207courseproject.use_case.ongoing_sets;

import com.example.csc207courseproject.data_access.DataAccessException;
import com.example.csc207courseproject.entities.EventData;

public class OngoingSetsInteractor implements OngoingSetsInputBoundary {

    private final OngoingSetsOutputBoundary ongoingSetsPresenter;
    private final OngoingSetsDataAccessInterface dataAccess;

    public OngoingSetsInteractor(OngoingSetsDataAccessInterface dataAccess,
                                 OngoingSetsOutputBoundary ongoingSetsPresenter) {
        this.dataAccess = dataAccess;
        this.ongoingSetsPresenter = ongoingSetsPresenter;
    }

    @Override
    public void execute() {
        // Check if API call is successful
        int eventId = EventData.getEventData().getEventId();
        try {
            OngoingSetsOutputData s = new OngoingSetsOutputData(dataAccess.getOngoingSets(eventId));

            ongoingSetsPresenter.prepareSuccessView(s);
        } catch (DataAccessException e) {
            ongoingSetsPresenter.prepareFailView();
        }
    }

}
