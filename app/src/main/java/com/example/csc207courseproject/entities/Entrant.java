package com.example.csc207courseproject.entities;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * An entity representing an entrant in an event
 */
public class Entrant {
    private final Participant[] participants;
    private final int entrantId;
    private final List<String> tags = new ArrayList<>();
    private Station currentStation; //The station a player is currently playing at

    /**
     * Entrant constructor.
     * @param participants Participants
     * @param entrantId Entrant ID
     */
    public Entrant(Participant[] participants, int entrantId) {
        this.participants = participants;
        this.entrantId = entrantId;
    }


    public int getId() {
        return entrantId;
    }

    @NotNull
    @Override
    public String toString() {
        String currPlayer = participants[0].toString();
        String output = currPlayer.trim();
        for (int i = 1; i < participants.length; i++) {
            currPlayer = participants[i].toString();
            output += " / " + currPlayer.trim();
        }
        return output;
    }

    public Station getCurrentStation() {
        return currentStation;
    }

    public void setCurrentStation(Station currentStation) {
        this.currentStation = currentStation;
    }

    public List<String> getTags() {return tags;}
}
