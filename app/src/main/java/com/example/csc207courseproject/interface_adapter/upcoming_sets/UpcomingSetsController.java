package com.example.csc207courseproject.interface_adapter.upcoming_sets;

import com.example.csc207courseproject.use_case.upcoming_sets.UpcomingSetsInputBoundary;

public class UpcomingSetsController {
    private final UpcomingSetsInputBoundary upcomingSetsUseCaseInteractor;

    public UpcomingSetsController(UpcomingSetsInputBoundary upcomingSetsUseCaseInteractor) {
        this.upcomingSetsUseCaseInteractor = upcomingSetsUseCaseInteractor;
    }


    /**
     * Execute the report game use case
     */
    public void execute() {
        upcomingSetsUseCaseInteractor.execute();

    }
}
