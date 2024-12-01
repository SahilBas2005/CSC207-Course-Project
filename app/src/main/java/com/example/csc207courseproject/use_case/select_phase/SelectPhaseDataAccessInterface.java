package com.example.csc207courseproject.use_case.select_phase;

import com.example.csc207courseproject.entities.Entrant;

import java.util.List;
import java.util.Map;

/**
 * The DAO for the select phase use case.
 */
public interface SelectPhaseDataAccessInterface {

    /**
     * Gets the seeding for the given phase.
     * @param phaseId The ID of the phase
     * @return A list of entrants in seeded order
     */
    List<Entrant> getSeedingInPhase(int phaseId);
}
