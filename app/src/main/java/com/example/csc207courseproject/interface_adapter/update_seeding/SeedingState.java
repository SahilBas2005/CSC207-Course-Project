package com.example.csc207courseproject.interface_adapter.update_seeding;

import com.example.csc207courseproject.entities.Entrant;

import java.util.List;

/**
 * A state containing all the data used for seeding
 */
public class SeedingState {
    private List<Entrant> seeding;
    private List<String> phaseNames;

    public void setSeeding(List<Entrant> newSeeding) {
        seeding = newSeeding;
    }

    public void setPhaseNames(List<String> newPhases) {
        phaseNames = newPhases;
    }

    /**
     * Get phase names.
     * @return Names of phases
     */
    public List<String> getPhaseNames() {
        return phaseNames;
    }

    public List<Entrant> getSeeding() {
        return seeding;
    }
}