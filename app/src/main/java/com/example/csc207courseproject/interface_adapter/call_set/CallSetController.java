package com.example.csc207courseproject.interface_adapter.call_set;

import android.util.Log;
import com.example.csc207courseproject.use_case.call_set.CallSetInputBoundary;
import com.example.csc207courseproject.use_case.call_set.CallSetInputData;

public class CallSetController {
    private final CallSetInputBoundary interactor;
    private final CallSetState state;

    public CallSetController(CallSetInputBoundary interactor, CallSetState state) {
        this.interactor = interactor;
        this.state = state;
    }


    /**
     * Execute the call set use case
     */
    public void execute() {
        CallSetInputData s = new CallSetInputData(state.getCurrentSet().getSetID(), state.getCurrentSet().getStation());
        interactor.execute(s);

    }
}
