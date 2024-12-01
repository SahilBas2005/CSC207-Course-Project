package com.example.csc207courseproject.use_case.tournament_description;

import com.cohere.api.Cohere;
import com.cohere.api.requests.GenerateRequest;
import com.cohere.api.types.Generation;
import com.example.csc207courseproject.BuildConfig;
import com.example.csc207courseproject.data_access.APIDataAccessObject;
import com.example.csc207courseproject.data_access.APIDataAccessObject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TournamentDescriptionInteractor implements TournamentDescriptionInputBoundary {

    private final TournamentDescriptionOutputBoundary tournamentDescriptionPresenter;
    private final APIDataAccessObject dataAccess;

    public TournamentDescriptionInteractor(APIDataAccessObject dataAccess, TournamentDescriptionOutputBoundary tournamentDescriptionPresenter) {
        this.dataAccess = dataAccess;
        this.tournamentDescriptionPresenter = tournamentDescriptionPresenter;
    }

    @Override
    public void execute(TournamentDescriptionInputData tournamentDescriptionInputData) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                String aiMessage = "This should be an ai message!";
                String query = "Generate a short and brief tournament description for a {name: 'Lecture Skipping Bracket', players: '12', type: '1v1', rounds: 'Double-Elimination'. note: just write the direct description as output nothing else.";
                Cohere cohere = Cohere.builder().token(BuildConfig.COHERE).build();
                Generation response = cohere.generate(GenerateRequest.builder().prompt(query).build());
                aiMessage = response.getGenerations().get(0).getText();
                TournamentDescriptionOutputData s = new TournamentDescriptionOutputData(aiMessage);
                tournamentDescriptionPresenter.prepareSuccessView(s);
            } catch (Exception e) {
                e.printStackTrace();
                tournamentDescriptionPresenter.prepareFailView("Something Went Wrong: " + e.getMessage());
            }
        });
    }
}
