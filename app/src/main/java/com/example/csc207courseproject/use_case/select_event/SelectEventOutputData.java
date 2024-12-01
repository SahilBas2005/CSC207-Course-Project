package com.example.csc207courseproject.use_case.select_event;

import com.example.csc207courseproject.entities.Entrant;
import com.example.csc207courseproject.entities.Participant;

import java.util.Map;
import java.util.SortedMap;

/**
 * Output data for the Select Event Use Case.
 */
public class SelectEventOutputData {

    private final Integer eventID;
    private final String eventName;
    private final Map<Integer, Entrant> entrants;
    private final Map<Integer, Participant> participants;
    private final boolean hasCharacters;
    private final SortedMap<String, Integer> phaseIDs;


    public SelectEventOutputData(Integer eventID, String eventName,
                                 Map<Integer, Entrant> entrants,
                                 Map<Integer, Participant> participants,
                                 boolean hasCharacters,
                                 SortedMap<String, Integer> phaseIDs) {
        this.eventID = eventID;
        this.eventName = eventName;
        this.entrants = entrants;
        this.participants = participants;
        this.hasCharacters = hasCharacters;
        this.phaseIDs = phaseIDs;
    }

    public Integer getEventID() {
        return eventID;
    }

    public String getEventName() {
        return eventName;
    }

    public Map<Integer, Entrant> getEntrants() {
        return entrants;
    }

    public Map<Integer, Participant> getParticipants() {
        return participants;
    }

    public boolean getHasCharacters() {
        return hasCharacters;
    }

    public SortedMap<String, Integer> getPhaseIDs() {
        return phaseIDs;
    }
}
