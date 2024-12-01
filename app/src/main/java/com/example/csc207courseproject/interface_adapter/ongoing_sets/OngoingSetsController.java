package com.example.csc207courseproject.interface_adapter.ongoing_sets;

import com.example.csc207courseproject.interface_adapter.report_set.ReportSetState;
import com.example.csc207courseproject.use_case.ongoing_sets.OngoingSetsInputBoundary;

public class OngoingSetsController {
    private final OngoingSetsInputBoundary ongoingSetsUseCaseInteractor;
    private final ReportSetState setState;

    public OngoingSetsController(OngoingSetsInputBoundary ongoingSetsUseCaseInteractor, ReportSetState setState) {
        this.ongoingSetsUseCaseInteractor = ongoingSetsUseCaseInteractor;
        this.setState = setState;
    }


    /**
     * Execute the report game use case
     */
    public void execute() {
        ongoingSetsUseCaseInteractor.execute();

    }
}
