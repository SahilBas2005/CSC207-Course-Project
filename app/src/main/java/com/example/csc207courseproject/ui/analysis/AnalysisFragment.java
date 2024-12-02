package com.example.csc207courseproject.ui.analysis;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.csc207courseproject.databinding.FragmentAnalysisBinding;
import com.example.csc207courseproject.interface_adapter.tournament_description.TournamentDescriptionController;
import com.example.csc207courseproject.interface_adapter.player_statistics.PlayerStatisitcsController;
import com.example.csc207courseproject.interface_adapter.tournament_description.TournamentState;
import com.example.csc207courseproject.ui.AppFragment;
import com.example.csc207courseproject.ui.seeding.SeedingViewModel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class AnalysisFragment extends AppFragment implements PropertyChangeListener {

    private static AnalysisViewModel analysisViewModel;
    private static TournamentDescriptionController tournamentDescriptionController;
    private static PlayerStatisitcsController playerStatisitcsController;

    private FragmentAnalysisBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {

        analysisViewModel.addPropertyChangeListener(this);

        binding = FragmentAnalysisBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        createTournamentDescriptionButton();
        return root;
    }

    private void createTournamentDescriptionButton() {
        Button tournamentDescriptionButton = binding.generateTournamentDescriptionBtn;
        tournamentDescriptionButton.setOnClickListener(view -> {
                tournamentDescriptionController.execute();
                showToast("Generating Description");
                displayTournamentDescription();
        });
    }

    private void displayTournamentDescription() {
        TextView aiMessage = binding.tournamentDescriptionResult;
        TournamentState currentState = analysisViewModel.getState();
        String reponse = currentState.getaiMessage();
        aiMessage.setText(reponse);
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case "updatesuccess": displayTournamentDescription(); break;
            case "updatefail": showToast("AI is sad today try again tmrw!"); break;
        }
    }

    public static void setTournamentDescriptionController(TournamentDescriptionController controller) {
        tournamentDescriptionController = controller;
    }


@Override
    public void onDestroyView() {
        super.onDestroyView();
        analysisViewModel.removePropertyChangeListener(this);
        binding = null;
    }

    public static void setAnalysisViewModel(AnalysisViewModel viewModel) {analysisViewModel = viewModel;}
}