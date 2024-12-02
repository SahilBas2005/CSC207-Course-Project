package com.example.csc207courseproject.use_case.get_phases;


import com.example.csc207courseproject.entities.EventData;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

public class GetPhasesInteractor implements GetPhasesInputBoundary {

    private final GetPhasesOutputBoundary presenter;

    public GetPhasesInteractor(GetPhasesOutputBoundary presenter) {
        this.presenter = presenter;
    }

    /**
     * Execute the logic for the get phases use case.
     */
    @Override
    public void execute() {
        // Get the phases from eventData
        SortedMap<String, Integer> phaseMap = EventData.getEventData().getPhaseIds();

        if (phaseMap == null){
            presenter.prepareFailView();
        } else {
            List<String> phases = new ArrayList<>(phaseMap.keySet());
            presenter.prepareSuccessView(new GetPhasesOutputData(phases));
        }
    }

}
