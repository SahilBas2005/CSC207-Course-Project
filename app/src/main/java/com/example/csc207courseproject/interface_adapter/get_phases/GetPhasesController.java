package com.example.csc207courseproject.interface_adapter.get_phases;

import com.example.csc207courseproject.use_case.get_phases.GetPhasesInputBoundary;

public class GetPhasesController {
    private final GetPhasesInputBoundary interactor;

    public GetPhasesController(GetPhasesInputBoundary interactor) {
        this.interactor = interactor;
    }


    /**
     * Execute the find station use case
     */
    public void execute() {
        interactor.execute();

    }
}
