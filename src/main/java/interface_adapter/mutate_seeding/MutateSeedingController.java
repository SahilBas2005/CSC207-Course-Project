package interface_adapter.mutate_seeding;

import use_case.mutate_seeding.MutateSeedingInputData;
import use_case.mutate_seeding.MutateSeedingInputBoundary;
import interface_adapter.update_seeding.SeedingState;

import java.util.List;

/**
 * The controller for the Mutate Seeding Use case
 */
public class MutateSeedingController {

    private final MutateSeedingInputBoundary mutateSeedingUseCaseInteractor;
    private final SeedingState seedingState;

    public MutateSeedingController(MutateSeedingInputBoundary mutateSeedingUseCaseInteractor, SeedingState seedingState) {
        this.mutateSeedingUseCaseInteractor = mutateSeedingUseCaseInteractor;
        this.seedingState = seedingState;
    }

    /**
     * Execute the report set use case
     */
    public void execute() {

        List<Integer> seeding = seedingState.getSeeding();
        final MutateSeedingInputData selectPhaseInputData = new MutateSeedingInputData(seeding);

        mutateSeedingUseCaseInteractor.execute(selectPhaseInputData);
    }
}

