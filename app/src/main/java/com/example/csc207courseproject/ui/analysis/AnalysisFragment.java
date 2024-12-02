package com.example.csc207courseproject.ui.analysis;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.csc207courseproject.databinding.FragmentAnalysisBinding;
import com.example.csc207courseproject.interface_adapter.tournament_description.TournamentDescriptionController;
import com.example.csc207courseproject.interface_adapter.tournament_description.TournamentState;
import com.example.csc207courseproject.ui.seeding.SeedingViewModel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class AnalysisFragment extends Fragment implements PropertyChangeListener {

    private static AnalysisViewModel analysisViewModel;
    private static TournamentDescriptionController tournamentDescriptionController;

    private FragmentAnalysisBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        analysisViewModel = new AnalysisViewModel();
        analysisViewModel.addPropertyChangeListener(this);

        binding = FragmentAnalysisBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        createTournamentDescriptionButton();
        return root;
    }

    private void createTournamentDescriptionButton() {
        Button tournamentDescriptionButton = binding.generateTournamentDescriptionBtn;
        tournamentDescriptionButton.setOnClickListener(view -> {
            if (tournamentDescriptionController != null) {
                tournamentDescriptionController.execute();
                showToast("Generating Description");

                displayTournamentDescription();
            }
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

    private void showToast(String message) {
        if (getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }


    public static void setTournamentDescriptionController(TournamentDescriptionController controller) {
        tournamentDescriptionController = controller;
    }

    public static AnalysisViewModel getAnalysisViewModel(){
        return analysisViewModel;
    }

@Override
    public void onDestroyView() {
        super.onDestroyView();
        analysisViewModel.removePropertyChangeListener(this);
        binding = null;
    }
}