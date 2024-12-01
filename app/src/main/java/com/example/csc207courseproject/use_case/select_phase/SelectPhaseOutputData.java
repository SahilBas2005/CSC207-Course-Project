package com.example.csc207courseproject.use_case.select_phase;

import com.example.csc207courseproject.entities.Entrant;

import java.util.List;

/**
 * The Output Data for the Select Phase Use Case.
 */
public class SelectPhaseOutputData {

    private List<Entrant> seeds;

    public SelectPhaseOutputData(List<Entrant> seeds) {
        this.seeds = seeds;
    }

    public List<Entrant> getSeeding() {
        return this.seeds;
    }

}