package com.example.csc207courseproject.interface_adapter.decline_set;

import com.example.csc207courseproject.interface_adapter.call_set.CallSetState;
import com.example.csc207courseproject.use_case.decline_set.DeclineSetInputBoundary;
import com.example.csc207courseproject.use_case.decline_set.DeclineSetInputData;;

public class DeclineSetController {
    private final DeclineSetInputBoundary interactor;
    private final CallSetState state;

    public DeclineSetController(DeclineSetInputBoundary interactor, CallSetState state) {
        this.interactor = interactor;
        this.state = state;
    }


    /**
     * Execute the decline set use case
     */
    public void execute(String tag, boolean p1Applies, boolean p2Applies) {
        DeclineSetInputData s = new DeclineSetInputData(tag, p1Applies, p2Applies, state.getCurrentSet());
        interactor.execute(s);

    }
}
