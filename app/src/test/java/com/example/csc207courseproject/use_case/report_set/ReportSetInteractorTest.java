package com.example.csc207courseproject.use_case.report_set;

import com.example.csc207courseproject.data_access.api.APIDataAccessException;
import com.example.csc207courseproject.data_access.api.APIDataAccessObject;
import com.example.csc207courseproject.entities.Entrant;
import com.example.csc207courseproject.entities.Game;
import com.example.csc207courseproject.entities.ReportSetData;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class ReportSetInteractorTest {

    @Test
    void mutateFailureTest() {

        // Create example input data
        Entrant en1 = new Entrant(null, 1);
        Entrant en2 = new Entrant(null, 2);
        ReportSetData exampleSet = new ReportSetData(1, new Entrant[] {en1, en2}, 5);
        ReportSetInputData inputData = new ReportSetInputData(1, 1, exampleSet, false);

        // Create new reportSeeding method which throws an exception for this test
        ReportSetDataAccessInterface failDataAccessInterface = new APIDataAccessObject() {
            @Override
            public void reportSet(int setID, int winnerId, List<Game> games, boolean hasDQ,
                                  int p1EntrantID, int p2EntrantID) {
                throw new APIDataAccessException("Failure");
            }
        };

        ReportSetOutputBoundary failPresenter = new ReportSetOutputBoundary() {
            @Override
            public void prepareSuccessView() {
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String error) {
                assertEquals("apicallerror", error);
            }
        };

        ReportSetInputBoundary interactor = new ReportSetInteractor(failDataAccessInterface, failPresenter);
        interactor.execute(inputData);
    }

    @Test
    void incompleteSetTest() {

        // Create example input data
        Entrant en1 = new Entrant(null, 1);
        Entrant en2 = new Entrant(null, 2);
        ReportSetData exampleSet = new ReportSetData(1, new Entrant[] {en1, en2}, 5);
        ReportSetInputData inputData = new ReportSetInputData(1, -1, exampleSet, false);


        // Create new reportSeeding method which throws an exception for this test
        ReportSetDataAccessInterface failDataAccessInterface = new APIDataAccessObject() {
            @Override
            public void reportSet(int setID, int winnerId, List<Game> games, boolean hasDQ,
            int p1EntrantID, int p2EntrantID) {
                fail("Use case should not report an incomplete set.");
            }
        };

        ReportSetOutputBoundary failPresenter = new ReportSetOutputBoundary() {
            @Override
            public void prepareSuccessView() {
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String error) {
                assertEquals("incompletesetinfo", error);
            }
        };

        ReportSetInputBoundary interactor = new ReportSetInteractor(failDataAccessInterface, failPresenter);
        interactor.execute(inputData);
    }

    @Test
    void successTest() {

        final boolean[] reportMade = {false};

        // Create example input data
        Entrant en1 = new Entrant(null, 1);
        Entrant en2 = new Entrant(null, 2);
        ReportSetData exampleSet = new ReportSetData(1, new Entrant[] {en1, en2}, 5);
        ReportSetInputData inputData = new ReportSetInputData(1, 1, exampleSet, false);

        // Create new reportSeeding method which returns for this test
        ReportSetDataAccessInterface successDataAccessInterface = new APIDataAccessObject() {
            @Override
            public void reportSet(int setID, int winnerId, List<Game> games, boolean hasDQ,
                                  int p1EntrantID, int p2EntrantID) {
                reportMade[0] = true;
            }
        };

        ReportSetOutputBoundary successPresenter = new ReportSetOutputBoundary() {
            @Override
            public void prepareSuccessView() {
                assert reportMade[0];
            }

            @Override
            public void prepareFailView(String error) {
                fail("Use case failure is unexpected.");
            }
        };

        ReportSetInputBoundary interactor = new ReportSetInteractor(successDataAccessInterface, successPresenter);
        interactor.execute(inputData);
    }

}
