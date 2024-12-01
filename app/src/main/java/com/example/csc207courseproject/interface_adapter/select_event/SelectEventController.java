package com.example.csc207courseproject.interface_adapter.select_event;

import com.example.csc207courseproject.use_case.select_event.SelectEventInputBoundary;

/**
 * Controller for the Select Event Use Case.
 */
public class SelectEventController {

    private final SelectEventInputBoundary selectEventInteractor;

    public SelectEventController(SelectEventInputBoundary selectEventInteractor) {
        this.selectEventInteractor = selectEventInteractor;
    }

    public void execute(Integer tournamentId, String selectedEventName, Integer selectedEventID) {
        selectEventInteractor.execute(tournamentId, selectedEventName, selectedEventID);
    }
}
