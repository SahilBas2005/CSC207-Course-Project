package com.example.csc207courseproject.interface_adapter.ongoing_sets;

import com.example.csc207courseproject.use_case.ongoing_sets.OngoingSetsInputBoundary;

public class OngoingSetsController {
    private final OngoingSetsInputBoundary ongoingSetsUseCaseInteractor;

    public OngoingSetsController(OngoingSetsInputBoundary ongoingSetsUseCaseInteractor) {
        this.ongoingSetsUseCaseInteractor = ongoingSetsUseCaseInteractor;
    }


    /**
     * Execute the report game use case
     */
    public void execute() {
        ongoingSetsUseCaseInteractor.execute();

    }
}
