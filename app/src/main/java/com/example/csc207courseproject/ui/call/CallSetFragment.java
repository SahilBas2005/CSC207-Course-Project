package com.example.csc207courseproject.ui.call;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.example.csc207courseproject.databinding.FragmentCallSetBinding;
import com.example.csc207courseproject.entities.EventData;
import com.example.csc207courseproject.interface_adapter.call_set.CallSetController;
import com.example.csc207courseproject.interface_adapter.call_set.CallSetState;
import com.example.csc207courseproject.interface_adapter.decline_set.DeclineSetController;
import com.example.csc207courseproject.ui.AppFragment;
import org.jetbrains.annotations.NotNull;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public class CallSetFragment extends AppFragment implements PropertyChangeListener, AdapterView.OnItemSelectedListener {

    private static CallViewModel callViewModel;
    private static CallSetController callSetController;
    private static DeclineSetController declineSetController;

    private FragmentCallSetBinding binding;
    private NavController navc;

    private String selectedOption;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        callViewModel.addPropertyChangeListener(this);

        binding = FragmentCallSetBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        createSetTitle();
        createCallButton();
        createDeclineMenu();
        createStreamButton();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navc = Navigation.findNavController(view);
    }

    public void createSetTitle(){
        CallSetState currentState = callViewModel.getState();
        TextView text = binding.selectedText;
        String outputText = currentState.getCurrentSet().toString();
        text.setText(outputText);
    }

    public void createCallButton(){
        Button callButton = binding.callButton;
        callButton.setOnClickListener(view -> callSetController.execute());
    }

    public void createDeclineMenu(){
        CallSetState currentState = callViewModel.getState();
        // Create dropdown
        Spinner tagsView = binding.declineTagSelect;
        List<String> tags = EventData.getEventData().getPossibleTags();
        ArrayAdapter<String> tagsAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_dropdown_item,
                tags);
        tagsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tagsView.setAdapter(tagsAdapter);
        tagsView.setOnItemSelectedListener(this);

        // Create button
        Button declineButton = binding.declineButton;
        declineButton.setOnClickListener(view -> {
            boolean p1Applied = binding.appliesP1.isChecked();
            boolean p2Applied = binding.appliesP2.isChecked();
            declineSetController.execute(selectedOption, p1Applied, p2Applied);
        });
    }

    public void createStreamButton(){
        CallSetState currentState = callViewModel.getState();
        // Only display the stream button if streams are open
        if (currentState.getOpenStream() != null) {
            binding.streamButton.setVisibility(View.VISIBLE);
        } else {
            binding.streamButton.setVisibility(View.INVISIBLE);
        }
        Button streamButton = binding.streamButton;
        streamButton.setOnClickListener(view -> {

            // Set the current set's station as the stream setup
            currentState.getCurrentSet().setStation(currentState.getOpenStream());
            createSetTitle();

        });
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
            case "callsuccess":
            case "declinesuccess":
                navc.navigateUp(); break;
            case "callfail": showToast("Could not reach the api. Please try again."); break;
        }
    }


    public static void setCallSetController(CallSetController controller) {
        callSetController = controller;
    }

    public static void setCallViewModel(CallViewModel viewModel) {
        callViewModel = viewModel;
    }

    public static void setDeclineSetController(DeclineSetController controller) {declineSetController = controller;}

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        selectedOption = (String) adapterView.getItemAtPosition(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
