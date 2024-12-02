package com.example.csc207courseproject.interface_adapter.upcoming_sets;

import com.example.csc207courseproject.interface_adapter.call_set.CallSetState;
import com.example.csc207courseproject.ui.call.CallViewModel;
import com.example.csc207courseproject.use_case.upcoming_sets.UpcomingSetsOutputBoundary;
import com.example.csc207courseproject.use_case.upcoming_sets.UpcomingSetsOutputData;

public class UpcomingSetsPresenter implements UpcomingSetsOutputBoundary {

    private final CallViewModel viewModel;

    public UpcomingSetsPresenter(CallViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(UpcomingSetsOutputData outputData) {
        final CallSetState currentState = viewModel.getState();

        currentState.setUpcomingSets(outputData.getUpcomingSets());
        viewModel.firePropertyChanged("getsetssuccess");
    }

    @Override
    public void prepareFailView() {
        viewModel.firePropertyChanged("getsetsfail");
    }

}
