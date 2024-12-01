package com.example.csc207courseproject.interface_adapter.upcoming_sets;

import com.example.csc207courseproject.interface_adapter.call_set.CallSetState;
import com.example.csc207courseproject.use_case.upcoming_sets.UpcomingSetsInputBoundary;
import com.example.csc207courseproject.use_case.upcoming_sets.UpcomingSetsInputData;

public class UpcomingSetsController {
    private final UpcomingSetsInputBoundary upcomingSetsUseCaseInteractor;
    private final CallSetState state;

    public UpcomingSetsController(UpcomingSetsInputBoundary upcomingSetsUseCaseInteractor, CallSetState state) {
        this.upcomingSetsUseCaseInteractor = upcomingSetsUseCaseInteractor;
        this.state = state;
    }


    /**
     * Execute the upcoming sets use case
     */
    public void execute() {
        upcomingSetsUseCaseInteractor.execute(new UpcomingSetsInputData(state.getCalledSetIDs()));

    }
}
