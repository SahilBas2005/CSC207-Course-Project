package com.example.csc207courseproject.ui.call;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import com.example.csc207courseproject.R;
import com.example.csc207courseproject.databinding.FragmentCallStationsBinding;
import com.example.csc207courseproject.entities.EventData;
import com.example.csc207courseproject.entities.Station;
import com.example.csc207courseproject.interface_adapter.add_station.AddStationController;
import com.example.csc207courseproject.interface_adapter.call_set.CallSetState;
import com.example.csc207courseproject.ui.AppFragment;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public class CallStationFragment extends AppFragment implements PropertyChangeListener{

    private static CallViewModel callViewModel;

    private FragmentCallStationsBinding binding;

    private static AddStationController addStationController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        callViewModel.addPropertyChangeListener(this);

        binding = FragmentCallStationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        createNewTagEntry();
        createAddButton();
        createStationDisplay();

        return root;
    }

    public void createStationDisplay(){
        CallSetState currentState = callViewModel.getState();
        ListView stationsView = binding.stationsList;
        List<Station> stations = currentState.getStations();

        if(stations.isEmpty()){
            binding.noStations.setVisibility(View.VISIBLE);
        }else{
            binding.noStations.setVisibility(View.INVISIBLE);
        }

        ArrayAdapter<Station> stationAdapter =
                new ArrayAdapter<Station>(mContext, android.R.layout.simple_list_item_1, stations) {
            @Override
            public int getCount() {
                return stations.size();
            }

            @Override
            public Station getItem(int position) {
                return stations.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @SuppressLint("ViewHolder")
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                Station station = stations.get(position);

                convertView = getLayoutInflater().inflate(R.layout.list_stations, parent, false);

                // Create title
                TextView title = convertView.findViewById(R.id.station_title);
                String stationText = "Station " + station.getStationNum();
                title.setText(stationText);

                // Create tags list
                TextView tags = convertView.findViewById(R.id.station_tags);
                String tagsText = "Tags: " + station.tagsToString();
                tags.setText(tagsText);

                // Create tag select
                Spinner tagSelect = convertView.findViewById(R.id.tag_select);
                List<String> possibleTags = EventData.getEventData().getPossibleTags();
                ArrayAdapter<String> tagsAdapter = new ArrayAdapter<>(mContext,
                        android.R.layout.simple_spinner_dropdown_item, possibleTags);
                tagsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                tagSelect.setAdapter(tagsAdapter);

                // Create add button
                Button addButton = convertView.findViewById(R.id.add_button);
                addButton.setOnClickListener(view -> {
                    station.addTag(tagSelect.getSelectedItem().toString());
                    createStationDisplay();
                });

                // Create remove button
                Button removeButton = convertView.findViewById(R.id.remove_button);
                removeButton.setOnClickListener(view -> {
                    station.removeTag(tagSelect.getSelectedItem().toString());
                    createStationDisplay();
                });

                return convertView;
            }
        };

        stationsView.setAdapter(stationAdapter);
    }

    private void createNewTagEntry(){
        EditText newTagInput = binding.newTag;
        Button submitButton = binding.submitTag;

        //Add the new tag to event data
        submitButton.setOnClickListener(view -> {
            EventData.getEventData().addPossibleTag(newTagInput.getText().toString());
            showToast("Added " + newTagInput.getText().toString() + " to tags.");
            createStationDisplay();
        });
    }

    private void createAddButton(){
        Button addButton = binding.addStationButton;
        addButton.setOnClickListener(view -> addStationController.execute());
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
            case "addsuccess": createStationDisplay(); break;
            case "addfail": showToast("Could not add station."); break;
        }
    }

    public static void setCallViewModel(CallViewModel viewModel) {
        callViewModel = viewModel;
    }

    public static void setAddStationController(AddStationController controller) {
        addStationController = controller;
    }
}