package com.example.csc207courseproject.use_case.decline_set;

import com.example.csc207courseproject.entities.CallSetData;

public class DeclineSetInteractor implements DeclineSetInputBoundary {

    private final DeclineSetOutputBoundary presenter;

    public DeclineSetInteractor(DeclineSetOutputBoundary presenter) {
        this.presenter = presenter;
    }

    /**
     * Execute the logic for the decline set use case.
     * @param inputData The input data
     */
    @Override
    public void execute(DeclineSetInputData inputData) {
        String tag = inputData.getTag();
        boolean p1Applies = inputData.getP1Applies();
        boolean p2Applies = inputData.getP2Applies();
        CallSetData currentSet = inputData.getCurrentSet();

        // Apply the tag to player one if their checkbox was ticked
        if (p1Applies && !currentSet.getPlayers()[0].getTags().contains(tag)) {
            currentSet.getPlayers()[0].getTags().add(tag);
        }
        // Apply the tag to player two if their checkbox was ticked
        if (p2Applies  && !currentSet.getPlayers()[1].getTags().contains(tag)){
            currentSet.getPlayers()[1].getTags().add(tag);
        }
        // Apply the tag to the station if it doesn't have it already
        if (!currentSet.getStation().hasTag(tag)) {
            currentSet.getStation().addTag(tag);
        }

        presenter.prepareSuccessView();

    }

}
