package com.example.csc207courseproject.ui.seeding;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import com.example.csc207courseproject.databinding.FragmentSeedingBinding;
import com.example.csc207courseproject.entities.Entrant;
import com.example.csc207courseproject.interface_adapter.get_phases.GetPhasesController;
import com.example.csc207courseproject.interface_adapter.mutate_seeding.MutateSeedingController;
import com.example.csc207courseproject.interface_adapter.select_phase.SelectPhaseController;
import com.example.csc207courseproject.interface_adapter.update_seeding.SeedingState;
import com.example.csc207courseproject.interface_adapter.update_seeding.UpdateSeedingController;
import com.example.csc207courseproject.ui.AppFragment;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class SeedingFragment extends AppFragment implements PropertyChangeListener, AdapterView.OnItemSelectedListener {

    private static SeedingViewModel seedingViewModel;
    private static GetPhasesController getPhasesController;
    private static SelectPhaseController selectPhaseController;
    private static UpdateSeedingController updateSeedingController;
    private static MutateSeedingController mutateSeedingController;
    private FragmentSeedingBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        Log.d("here", "here");
        seedingViewModel.addPropertyChangeListener(this);

        binding = FragmentSeedingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Get phases on start up
        getPhasesController.execute();

        createSeedingMenu();
        createMutateButton();


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        seedingViewModel.removePropertyChangeListener(this);
        binding = null;
    }

    private void createSeedDisplay() {
        SeedingState currentState = seedingViewModel.getState();
        List<String> seeds = new ArrayList<>();
        ListView seedsView = binding.seedsList;
        List<Entrant> seeding = currentState.getSeeding();
        if(seeding != null) {
            for (int i = 0; i < seeding.size(); i++) {
                seeds.add((i + 1) + ". " + seeding.get(i).toString());
            }
        }
        ArrayAdapter<String> seedsAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, seeds);
        seedsView.setAdapter(seedsAdapter);
    }

    private void createPhaseSelect() {
        SeedingState currentState = seedingViewModel.getState();
        Spinner phaseView = binding.phaseSelect;
        List<String> phases = currentState.getPhaseNames();
        ArrayAdapter<String> phaseAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_dropdown_item, phases);
        phaseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        phaseView.setAdapter(phaseAdapter);
        phaseView.setOnItemSelectedListener(this);
    }

    private void createSeedingMenu(){
        EditText newSeedInput;
        EditText oldSeedInput;
        oldSeedInput = binding.oldSeed;
        newSeedInput = binding.newSeed;
        Button confirmButton = binding.confirmButton;

        confirmButton.setOnClickListener(view -> {
            String oldSeedValue = oldSeedInput.getText().toString();
            String newSeedValue = newSeedInput.getText().toString();
            updateSeedingController.execute(
                    oldSeedValue, newSeedValue);
        });
    }

    private void createMutateButton(){
        Button mutateButton = binding.mutateButton;
        mutateButton.setOnClickListener(view -> {
            mutateSeedingController.execute();
        });
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case "seedfail": showToast("Error finding seeds."); break;
            case "seedsuccess":
            case "updatesuccess":
                createSeedDisplay(); break;
            case "mutatefail": showToast("The Start.gg API could not be reached. Seeding was not updated."); break;
            case "mutatesuccess": showToast("The seeding has been successfully mutated on Start.gg!");
                break;
            case "updatefail": showToast("Invalid seed. Try again."); break;
            case "getphasessuccess": createPhaseSelect(); break;
            case "getphasesfail": showToast("Could nor findin phases."); break;
        }
    }

    public static void setUpdateSeedingController(UpdateSeedingController controller) {
        updateSeedingController = controller;
    }

    public static void setSelectPhaseController(SelectPhaseController controller) {
        selectPhaseController = controller;
    }

    public static void setMutateSeedingController(MutateSeedingController controller) {
        mutateSeedingController = controller;
    }

    public static void setGetPhasesController(GetPhasesController controller) {
        getPhasesController = controller;
    }

    public static void setSeedingViewModel(SeedingViewModel viewModel) {
        seedingViewModel = viewModel;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        String selectedOption = (String) parent.getItemAtPosition(pos);
        selectPhaseController.execute(selectedOption);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}