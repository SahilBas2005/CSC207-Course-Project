package com.example.csc207courseproject.use_case.ongoing_sets;

import com.example.csc207courseproject.data_access.api.APIDataAccessException;
import com.example.csc207courseproject.data_access.api.APIDataAccessObject;
import com.example.csc207courseproject.entities.EventData;
import com.example.csc207courseproject.entities.ReportSetData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;

class OngoingSetsInteractorTest {

    @BeforeAll
    static void setUpBeforeClass() {
        // Create test EventData
        EventData.createEventData(0,0, "", null, null, null, null);
    }

    @Test
    void mutateFailureTest() {
        // Create new getOngoingSets method which throws an API Data access exception for this test
        OngoingSetsDataAccessInterface failDataAccessInterface = new APIDataAccessObject() {
            @Override
            public List<ReportSetData> getOngoingSets(int eventId) {
                throw new APIDataAccessException("Failure");
            }
        };

        OngoingSetsOutputBoundary failPresenter = new OngoingSetsOutputBoundary() {
            @Override
            public void prepareSuccessView(OngoingSetsOutputData outputData) {
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView() {
                assert true;
            }
        };

        OngoingSetsInputBoundary interactor = new OngoingSetsInteractor(failDataAccessInterface, failPresenter);
        interactor.execute();
    }

    @Test
    void successTest() {
        // Create new getOngoingSets method which returns for this test
        OngoingSetsDataAccessInterface successDataAccessInterface = new APIDataAccessObject() {
            @Override
            public List<ReportSetData> getOngoingSets(int eventId) {
                return new ArrayList<>();
            }
        };

        OngoingSetsOutputBoundary successPresenter = new OngoingSetsOutputBoundary() {
            @Override
            public void prepareSuccessView(OngoingSetsOutputData outputData) {
                assert outputData != null;
            }

            @Override
            public void prepareFailView() {
                fail("Use case failure is unexpected.");
            }
        };

        OngoingSetsInputBoundary interactor = new OngoingSetsInteractor(successDataAccessInterface, successPresenter);
        interactor.execute();
    }

}
