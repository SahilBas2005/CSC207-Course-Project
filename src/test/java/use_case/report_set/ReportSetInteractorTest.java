package use_case.report_set;

import data_access.APIDataAccessObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class ReportSetInteractorTest {

    @Test
    void mutateFailureTest() {

        // Example
        ReportSetInputData inputData = new ReportSetInputData();

        // Create new reportSeeding method which throws an exception for this test
        APIDataAccessObject failDataAccessInterface = new APIDataAccessObject() {
            @Override
            public void reportSet() {
                throw new UnsupportedOperationException("Failure");
            }
        };

        ReportSetOutputBoundary failPresenter = new ReportSetOutputBoundary() {
            @Override
            public void prepareSuccessView() {
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String error) {
                assertEquals("Something went wrong with the API call, try again.", error);
            }
        };

        ReportSetInputBoundary interactor = new ReportSetInteractor(failDataAccessInterface, failPresenter);
        interactor.execute(inputData);
    }

}
