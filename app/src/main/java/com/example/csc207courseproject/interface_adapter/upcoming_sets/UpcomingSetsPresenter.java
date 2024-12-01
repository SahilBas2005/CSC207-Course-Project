package com.example.csc207courseproject.interface_adapter.upcoming_sets;

import android.util.Log;
import com.example.csc207courseproject.entities.SetData;
import com.example.csc207courseproject.interface_adapter.ViewManagerModel;
import com.example.csc207courseproject.interface_adapter.call_set.CallSetState;
import com.example.csc207courseproject.ui.call.CallViewModel;
import com.example.csc207courseproject.use_case.upcoming_sets.UpcomingSetsOutputBoundary;
import com.example.csc207courseproject.use_case.upcoming_sets.UpcomingSetsOutputData;

import java.util.ArrayList;
import java.util.List;

public class UpcomingSetsPresenter implements UpcomingSetsOutputBoundary {

    private final CallViewModel viewModel;
    private final ViewManagerModel viewManagerModel;

    public UpcomingSetsPresenter(CallViewModel viewModel, ViewManagerModel viewManagerModel) {
        this.viewModel = viewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void prepareSuccessView(UpcomingSetsOutputData outputData) {
        final CallSetState currentState = viewModel.getState();

        List<SetData> sets = outputData.getUpcomingSets();
        List<SetData> repeats = new ArrayList<>();

        // Remove the recently called sets from the state's ongoing sets variable

        for (SetData set : sets) {
            if (currentState.getCalledSetIDs().contains(set.getSetID())) {
                repeats.add(set);
            }
        }

        sets.removeAll(repeats);

        currentState.setUpcomingSets(sets);
        viewModel.firePropertyChanged("getsetssuccess");
    }

    @Override
    public void prepareFailView(String errorMessage) {
        viewModel.firePropertyChanged(errorMessage);
    }

}
