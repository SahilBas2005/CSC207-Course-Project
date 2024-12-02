package com.example.csc207courseproject.interface_adapter.tournament_description;


import com.example.csc207courseproject.use_case.tournament_description.TournamentDescriptionOutputData;
import com.example.csc207courseproject.use_case.tournament_description.TournamentDescriptionOutputBoundary;
import com.example.csc207courseproject.ui.analysis.AnalysisViewModel;

public class TournamentDescriptionPresenter implements TournamentDescriptionOutputBoundary {

    private final AnalysisViewModel analysisViewModel;

    public TournamentDescriptionPresenter( AnalysisViewModel analysisViewModel) {
        this.analysisViewModel = analysisViewModel;
    }

    @Override
    public void prepareSuccessView(TournamentDescriptionOutputData outputData) {
        final String aiMessage = outputData.getAiMessage();
        System.out.println(aiMessage);
        final TournamentState tournamentState = analysisViewModel.getState();
        tournamentState.setaiMessage(aiMessage);
        analysisViewModel.firePropertyChanged("updatesuccess");
    }

    @Override
    public void prepareFailView(String errorMessage) {
        final TournamentState tournamentState = analysisViewModel.getState();
        tournamentState.setError(errorMessage);
        analysisViewModel.firePropertyChanged("updatefail");
    }
}
