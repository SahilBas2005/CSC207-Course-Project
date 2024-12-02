package com.example.csc207courseproject.use_case.tournament_description;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;


class TournamentDescriptionInteractorTest {

    @Test
    void generateTournamentDescriptionSuccessTest() {
        // Mock Output Boundary (Presenter)
        TournamentDescriptionOutputBoundary mockPresenter = new TournamentDescriptionOutputBoundary() {
            @Override
            public void prepareSuccessView(TournamentDescriptionOutputData outputData) {
                // Validate the message sent to the UI
                assertEquals("Tournament with 10 players is coming soon!",
                        outputData.getAiMessage(),
                        "The generated description should match the expected output.");
            }

            @Override
            public void prepareFailView(String error) {
                fail("Failure is not expected in this test.");
            }
        };

        // Mock Data Access (To simulate AI message generation)
        TournamentDescriptionDataAccessInterface mockDataAccess = new TournamentDescriptionDataAccessInterface() {
            @Override
            public String generateTournamentDescription(String eventName, int noOfPlayers) {
                return "Tournament with " + noOfPlayers + " players is coming soon!";
            }
        };

        // Create the interactor
        TournamentDescriptionInteractor interactor = new TournamentDescriptionInteractor(mockDataAccess, mockPresenter);

        // Execute the interactor
        interactor.execute();
    }
}
