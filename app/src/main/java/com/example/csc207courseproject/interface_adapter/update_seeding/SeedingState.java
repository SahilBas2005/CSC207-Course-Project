package com.example.csc207courseproject.interface_adapter.update_seeding;

import com.example.csc207courseproject.entities.EventData;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;

/**
 * A state containing all the data used for seeding
 */
public class SeedingState {
    private List<Integer> seeding;
    private SortedMap<String, Integer> phaseToID;
    private String error = "";

    public String playerIdToString(int playerID) {
        return EventData.entrantIdToString(playerID);
    }
    public void setSeeding(List<Integer> newSeeding) {
        seeding = newSeeding;
    }

    public void setPhases(SortedMap<String, Integer> newPhases) {
        phaseToID = newPhases;
    }

    /**
     * Get phase names.
     * @return Names of phases
     */
    public List<String> getPhases() {
        Set<String> phaseSet = phaseToID.keySet();
        return new ArrayList<String>(phaseSet);
    }

    /**
     * Converts from phase name to id.
     * @param phase Name of the phase
     * @return ID of the phase
     */
    public int phaseNametoId(String phase) {

        return phaseToID.get(phase);
    }

    public List<Integer> getSeeding() {
        return seeding;
    }

    /**
     * Mutates the seeding to move a seed from oldSeed to newSeed.
     */
    public void moveSeed(int oldSeed, int newSeed) {
        int tempID = seeding.get(oldSeed - 1);
        seeding.remove(oldSeed - 1);
        seeding.add(newSeed - 1, tempID);
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}