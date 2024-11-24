package com.example.csc207courseproject.use_case.mutate_seeding;

import java.util.List;

/**
 * The DMO for the mutate seeding use case.
 */
public interface MutateSeedingDataAccessInterface {

    /**
     * Mutates the seeding on start gg to match the parameter seeding.
     * @param seededEntrants A list in seeded order of player IDs for each entrant
     */
    void setSeeding(List<Integer> seededEntrants);
}
