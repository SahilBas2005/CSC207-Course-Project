package com.example.csc207courseproject.use_case.mutate_seeding;

import java.util.List;

/**
 * The Input Data for the Mutate Seeding to Start.gg Use Case.
 */
public class MutateSeedingInputData {

    private final List<Integer> finalSeeds;

    public MutateSeedingInputData(List<Integer> finalSeeds) {
        this.finalSeeds = finalSeeds;
    }

    public List<Integer> getFinalSeeds() {
        return finalSeeds;
    }

}
