package com.example.csc207courseproject.interface_adapter.select_event;

import com.example.csc207courseproject.use_case.select_event.SelectEventInputBoundary;

/**
 * Controller for the Select Event Use Case.
 */
public class SelectEventController {

    private final SelectEventInputBoundary selectEventInteractor;

    /**
     * The class constructor.
     * @param selectEventInteractor the interactor to set for selectEventInteractor
     */
    public SelectEventController(SelectEventInputBoundary selectEventInteractor) {
        this.selectEventInteractor = selectEventInteractor;
    }

    /**
     * Executes the Select Event Use Case.
     * @param tournamentId id of the tournament selected by the user
     * @param selectedEventName name of the event selected by the user
     * @param selectedEventID id of the event selected by the user
     */
    public void execute(Integer tournamentId, String selectedEventName, Integer selectedEventID) {
        selectEventInteractor.execute(tournamentId, selectedEventName, selectedEventID);
    }
}
