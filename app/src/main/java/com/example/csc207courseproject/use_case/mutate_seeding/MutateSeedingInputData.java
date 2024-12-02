package com.example.csc207courseproject.use_case.mutate_seeding;

import com.example.csc207courseproject.entities.Entrant;

import java.util.List;

/**
 * The Input Data for the Mutate Seeding to Start.gg Use Case.
 */
public class MutateSeedingInputData {

    private final List<Entrant> finalSeeds;

    public MutateSeedingInputData(List<Entrant> finalSeeds) {
        this.finalSeeds = finalSeeds;
    }

    public List<Entrant> getFinalSeeds() {
        return finalSeeds;
    }

}
