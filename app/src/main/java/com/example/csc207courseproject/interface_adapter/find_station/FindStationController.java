package com.example.csc207courseproject.interface_adapter.find_station;

import com.example.csc207courseproject.interface_adapter.call_set.CallSetState;
import com.example.csc207courseproject.use_case.find_station.FindStationInputBoundary;
import com.example.csc207courseproject.use_case.find_station.FindStationInputData;

public class FindStationController {
    private final FindStationInputBoundary interactor;
    private final CallSetState state;

    public FindStationController(FindStationInputBoundary interactor, CallSetState state) {
        this.interactor = interactor;
        this.state = state;
    }


    /**
     * Execute the find station use case
     */
    public void execute() {
        FindStationInputData s = new FindStationInputData(state.getCurrentSet(), state.getStations());
        interactor.execute(s);

    }
}
