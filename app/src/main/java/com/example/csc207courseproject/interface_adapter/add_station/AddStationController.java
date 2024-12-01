package com.example.csc207courseproject.interface_adapter.add_station;

import android.util.Log;
import com.example.csc207courseproject.interface_adapter.call_set.CallSetState;
import com.example.csc207courseproject.use_case.add_station.AddStationInputBoundary;
import com.example.csc207courseproject.use_case.add_station.AddStationInputData;

public class AddStationController {
    private final AddStationInputBoundary interactor;
    private final CallSetState state;

    public AddStationController(AddStationInputBoundary interactor, CallSetState state) {
        this.interactor = interactor;
        this.state = state;
    }


    /**
     * Execute the add station use case
     */
    public void execute() {
        AddStationInputData s = new AddStationInputData(state.getStations().size() + 1);
        interactor.execute(s);

    }
}
