package com.example.csc207courseproject.use_case.update_seeding;

/**
 * The Input Data for the Updating Seeding Locally Use Case.
 */
public class UpdateSeedingInputData {

    private int oldSeed;
    private int newSeed;
    private int maxSeed;

    public UpdateSeedingInputData(int oldSeed, int newSeed, int maxSeed) {
        this.oldSeed = oldSeed;
        this.newSeed = newSeed;
        this.maxSeed = maxSeed;
    }

    public int getOldSeed() {
        return oldSeed;
    }

    public int getNewSeed() {
        return newSeed;
    }

    public int getMaxSeed() {
        return maxSeed;
    }

}
