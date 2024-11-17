package interface_adapter.update_seeding;

import use_case.update_seeding.UpdateSeedingInputData;
import use_case.update_seeding.UpdateSeedingInputBoundary;

/**
 * The controller for the UpdateSeeding use case
 */
public class UpdateSeedingController {

    private final UpdateSeedingInputBoundary updateSeedingUseCaseInteractor;
    private final SeedingState seedingState;

    public UpdateSeedingController(UpdateSeedingInputBoundary updateSeedingUseCaseInteractor, SeedingState seedingState) {
        this.updateSeedingUseCaseInteractor = updateSeedingUseCaseInteractor;
        this.seedingState = seedingState;
    }

    public void execute(String oldSeed, String newSeed) {

        int maxSeed = seedingState.getSeeding().size();

        try {

            Integer.parseInt(oldSeed);
            Integer.parseInt(newSeed);
            int oldSeedValue = Integer.parseInt(oldSeed);
            int newSeedValue = Integer.parseInt(newSeed);

            final UpdateSeedingInputData updateSeedingInputData = new UpdateSeedingInputData(oldSeedValue, newSeedValue, maxSeed);
            updateSeedingUseCaseInteractor.execute(updateSeedingInputData);
        } catch (NumberFormatException e){
            // If the input isn't an integer, then pass an invalid integer as input data
            UpdateSeedingInputData failSeedingInputData = new UpdateSeedingInputData(-1, -1, maxSeed);
            updateSeedingUseCaseInteractor.execute(failSeedingInputData);
        }
    }
}