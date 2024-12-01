package com.example.csc207courseproject.use_case.select_event;

import com.example.csc207courseproject.data_access.api.APIDataAccessException;
import com.example.csc207courseproject.entities.Entrant;
import com.example.csc207courseproject.entities.EventData;
import com.example.csc207courseproject.entities.Participant;

import java.util.List;
import java.util.Map;
import java.util.SortedMap;

public class SelectEventInteractor implements SelectEventInputBoundary {

    private final SelectEventDataAccessInterface selectEventDataAccessObject;
    private final SelectEventOutputBoundary selectEventPresenter;

    public SelectEventInteractor(SelectEventDataAccessInterface selectEventDataAccessInterface,
                                 SelectEventOutputBoundary selectEventPresenter) {
        this.selectEventDataAccessObject = selectEventDataAccessInterface;
        this.selectEventPresenter = selectEventPresenter;
    }

    @Override
    public void execute(Integer tournamentId, String selectedEventName, Integer selectedEventID) {
        try {
            List<Object> eventData = selectEventDataAccessObject.getEventData(selectedEventID);
            EventData.createEventData(tournamentId, selectedEventID, selectedEventName,
                                        (Map<Integer, Entrant>) eventData.get(0),
                                        (Map<Integer, Participant>) eventData.get(1),
                                        (SortedMap<String, Integer>) eventData.get(2),
                                        (SortedMap<String, Integer>) eventData.get(3));
            selectEventPresenter.prepareSuccessView();
        }
        catch (APIDataAccessException e) {
            selectEventPresenter.prepareFailView();
        }

    }
}
