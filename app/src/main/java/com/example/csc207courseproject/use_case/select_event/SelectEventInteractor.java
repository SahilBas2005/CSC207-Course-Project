package com.example.csc207courseproject.use_case.select_event;

import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import com.example.csc207courseproject.data_access.DataAccessException;
import com.example.csc207courseproject.entities.Entrant;
import com.example.csc207courseproject.entities.EventData;
import com.example.csc207courseproject.entities.Participant;

/**
 * Interactor for the Select Event Use Case.
 */
public class SelectEventInteractor implements SelectEventInputBoundary {

    private final SelectEventDataAccessInterface selectEventDataAccessObject;
    private final SelectEventOutputBoundary selectEventPresenter;

    /**
     * The class constructor.
     * @param selectEventDataAccessInterface the DAO to set for selectEventDataAccessObject
     * @param selectEventPresenter the presenter to set for selectEventPresenter
     */
    public SelectEventInteractor(SelectEventDataAccessInterface selectEventDataAccessInterface,
                                 SelectEventOutputBoundary selectEventPresenter) {
        this.selectEventDataAccessObject = selectEventDataAccessInterface;
        this.selectEventPresenter = selectEventPresenter;
    }

    /**
     * Executes the Select Event Use Case.
     * @param tournamentId the tournament that the event belongs to
     * @param selectedEventName name of the event
     * @param selectedEventID id of the event
     */
    @Override
    public void execute(Integer tournamentId, String selectedEventName, Integer selectedEventID) {
        try {
            final List<Object> eventData = selectEventDataAccessObject.getEventData(selectedEventID);
            EventData.createEventData(tournamentId, selectedEventID, selectedEventName,
                                        (Map<Integer, Entrant>) eventData.get(0),
                                        (Map<Integer, Participant>) eventData.get(1),
                                        (SortedMap<String, Integer>) eventData.get(2),
                                        (SortedMap<String, Integer>) eventData.get(3));
            selectEventPresenter.prepareSuccessView();
        }
        catch (DataAccessException e) {
            selectEventPresenter.prepareFailView();
        }

    }
}
