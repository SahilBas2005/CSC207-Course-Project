package com.example.csc207courseproject.use_case.select_phase;

import java.util.List;

/**
 * The Output Data for the Select Phase Use Case.
 */
public class SelectPhaseOutputData {

    private List<Integer> seeds;

    public SelectPhaseOutputData(List<Integer> seeds) {
        this.seeds = seeds;
    }

    public List<Integer> getSeeding() {
        return this.seeds;
    }

}