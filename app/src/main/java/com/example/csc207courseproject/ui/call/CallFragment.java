package com.example.csc207courseproject.ui.call;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.example.csc207courseproject.R;
import com.example.csc207courseproject.databinding.FragmentCallBinding;
import com.example.csc207courseproject.entities.CallSetData;
import com.example.csc207courseproject.interface_adapter.call_set.CallSetState;
import com.example.csc207courseproject.interface_adapter.find_station.FindStationController;
import com.example.csc207courseproject.interface_adapter.get_stations.GetStationsController;
import com.example.csc207courseproject.interface_adapter.upcoming_sets.UpcomingSetsController;
import com.example.csc207courseproject.ui.AppFragment;
import org.jetbrains.annotations.NotNull;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class CallFragment extends AppFragment implements PropertyChangeListener {

    private static CallViewModel callViewModel;
    private static UpcomingSetsController upcomingSetsController;
    private static GetStationsController getStationsController;
    private static FindStationController findStationController;
    private NavController navc;

    private FragmentCallBinding binding;
    private boolean navReady = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {

        callViewModel.addPropertyChangeListener(this);

        binding = FragmentCallBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        if (callViewModel.getState().isEventStarted()) {
            // Set visibility
            binding.getSetsButton.setVisibility(View.INVISIBLE);
            binding.getSetsWarning.setVisibility(View.INVISIBLE);
            binding.setsText.setVisibility(View.VISIBLE);
            binding.configureButton.setVisibility(View.VISIBLE);

            upcomingSetsController.execute();
        } else {
            createGetSetsButton();
        }
        createStationButton();
        getStationsController.execute();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CallSetFragment.setCallViewModel(callViewModel);
        CallStationFragment.setCallViewModel(callViewModel);
        navc = Navigation.findNavController(view);
    }

    private void createGetSetsButton() {
        CallSetState currentState = callViewModel.getState();
        Button getSets = binding.getSetsButton;
        getSets.setOnClickListener(view -> {
            // Start the event and set the visibility of the view
            currentState.setEventStarted(true);
            upcomingSetsController.execute();
            getSets.setVisibility(View.INVISIBLE);
            binding.getSetsWarning.setVisibility(View.INVISIBLE);
            binding.setsText.setVisibility(View.VISIBLE);
            binding.configureButton.setVisibility(View.VISIBLE);
        });
    }

    private void createDisplay() {
        CallSetState currentState = callViewModel.getState();
        List<String> setDisplay = new ArrayList<>();
        ListView setsView = binding.upcomingSets;
        List<CallSetData> sets = currentState.getUpcomingSets();

        // If there are no current upcoming sets, then display that there are no upcoming sets
        // Otherwise, create the set display menu

        if(!sets.isEmpty()) {
            binding.noUpcomingSets.setVisibility(View.INVISIBLE);
            binding.previewMessage.setVisibility(View.INVISIBLE);
            for (CallSetData set : sets) {
                setDisplay.add(set.toString());
            }
        } else {
            binding.noUpcomingSets.setVisibility(View.VISIBLE);
            binding.previewMessage.setVisibility(View.VISIBLE);
        }

        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, setDisplay);
        setsView.setAdapter(itemsAdapter);
        setsView.setOnItemClickListener((list, view, position, id) -> {
            callViewModel.getState().setCurrentSet(sets.get(position));
            findStationController.execute();
            if (navReady) {
                navc.navigate(R.id.action_nav_call_to_callSetFragment);
                navReady = false;
            }
        });
    }

    private void createStationButton(){
        Button configureButton = binding.configureButton;
        configureButton.setOnClickListener(view -> navc.navigate(R.id.action_nav_call_to_callStationFragment));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        callViewModel.removePropertyChangeListener(this);
        binding = null;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case "getsetssuccess": createDisplay(); break;
            case "getsetsfail": break;
            case "getstationssuccess": break;
            case "getstationsfail": showToast("Stations can not be found."); break;
            case "findsuccess": navReady = true; break;
            case "findfail": showToast("No stations are available. Make sure stations are created."); break;
        }
    }

    public static void setUpcomingSetsController(UpcomingSetsController controller) {
        upcomingSetsController = controller;
    }

    public static void setGetStationsController(GetStationsController controller) {
        getStationsController = controller;
    }

    public static void setFindStationController(FindStationController controller) {
        findStationController = controller;
    }

    public static void setCallViewModel(CallViewModel viewModel) {
        callViewModel = viewModel;
    }
}