package com.example.csc207courseproject.use_case.ongoing_sets;


import android.util.Log;
import com.example.csc207courseproject.data_access.api.APIDataAccessException;
import com.example.csc207courseproject.entities.EventData;

import java.util.Arrays;

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
        int eventId = EventData.getEventId();
        try {
            OngoingSetsOutputData s = new OngoingSetsOutputData(dataAccess.getOngoingSets(eventId));

            ongoingSetsPresenter.prepareSuccessView(s);
        } catch (APIDataAccessException e) {
            ongoingSetsPresenter.prepareFailView("Something went wrong with the API call, try again.");
        }
    }

}
