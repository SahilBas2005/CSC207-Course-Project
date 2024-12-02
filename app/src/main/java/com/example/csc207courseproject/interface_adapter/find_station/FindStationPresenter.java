package com.example.csc207courseproject.interface_adapter.find_station;

import com.example.csc207courseproject.interface_adapter.call_set.CallSetState;
import com.example.csc207courseproject.ui.call.CallViewModel;
import com.example.csc207courseproject.use_case.find_station.FindStationOutputBoundary;
import com.example.csc207courseproject.use_case.find_station.FindStationOutputData;

public class FindStationPresenter implements FindStationOutputBoundary {

    private final CallViewModel viewModel;

    public FindStationPresenter(CallViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(FindStationOutputData outputData) {
        CallSetState currentState = viewModel.getState();
        currentState.getCurrentSet().setStation(outputData.getStation());
        currentState.setOpenStream(outputData.getOpenStream());
        viewModel.firePropertyChanged("findsuccess");
    }

    @Override
    public void prepareFailView() {
        viewModel.firePropertyChanged("findfail");
    }

}
