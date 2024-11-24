package com.example.csc207courseproject.use_case.update_seeding;

/**
 * Output Data for the Update Seeding Use Case.
 */
public class UpdateSeedingOutputData {
    private int oldSeed;
    private int newSeed;

    public UpdateSeedingOutputData(int oldSeed, int newSeed) {
        this.oldSeed = oldSeed;
        this.newSeed = newSeed;
    }

    public int getOldSeed() {
        return oldSeed;
    }

    public int getNewSeed() {
        return newSeed;
    }
}

