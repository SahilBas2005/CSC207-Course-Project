package com.example.csc207courseproject.use_case.tournament_description;

import com.example.csc207courseproject.entities.EventData;

public class TournamentDescriptionInteractor implements TournamentDescriptionInputBoundary {

    private final TournamentDescriptionOutputBoundary tournamentDescriptionPresenter;
    private final TournamentDescriptionDataAccessInterface dataAccess;

    public TournamentDescriptionInteractor(TournamentDescriptionDataAccessInterface dataAccess, TournamentDescriptionOutputBoundary tournamentDescriptionPresenter) {
        this.dataAccess = dataAccess;
        this.tournamentDescriptionPresenter = tournamentDescriptionPresenter;

    }

    /**
     * Executes the tournament description use case
     */
    @Override
    public void execute() {
        try {
            String eventName = EventData.getEventData().getEventName();
            int noOfPlayers = EventData.getEventData().getParticipants().size();

            // Delegate API call to Data Access Object
            String aiMessage = dataAccess.generateTournamentDescription(eventName, noOfPlayers);

            TournamentDescriptionOutputData outputData = new TournamentDescriptionOutputData(aiMessage);
            tournamentDescriptionPresenter.prepareSuccessView(outputData);

        } catch (Exception e) {
            // Handle failure scenario
            System.err.println("Error generating tournament description: " + e.getMessage());
            tournamentDescriptionPresenter.prepareFailView("Failed to generate tournament description.");
        }
    }
}