package com.example.csc207courseproject.interface_adapter.get_stations;

import com.example.csc207courseproject.interface_adapter.call_set.CallSetState;
import com.example.csc207courseproject.use_case.get_stations.GetStationsInputBoundary;
import com.example.csc207courseproject.use_case.get_stations.GetStationsInputData;

public class GetStationsController {
    private final GetStationsInputBoundary interactor;
    private final CallSetState state;

    public GetStationsController(GetStationsInputBoundary interactor, CallSetState state) {
        this.interactor = interactor;
        this.state = state;
    }


    /**
     * Execute the get stations use case
     */
    public void execute() {
        GetStationsInputData s = new GetStationsInputData(state.getLocalStations());
        interactor.execute(s);

    }
}
