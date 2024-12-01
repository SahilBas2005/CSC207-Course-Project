package com.example.csc207courseproject.interface_adapter.add_station;

import com.example.csc207courseproject.interface_adapter.call_set.CallSetState;
import com.example.csc207courseproject.ui.call.CallViewModel;
import com.example.csc207courseproject.use_case.add_station.AddStationOutputBoundary;
import com.example.csc207courseproject.use_case.add_station.AddStationOutputData;

public class AddStationPresenter implements AddStationOutputBoundary {

    private final CallViewModel viewModel;

    public AddStationPresenter(CallViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(AddStationOutputData outputData) {
        final CallSetState setState = viewModel.getState();
        setState.addStation(outputData.getStation());
        viewModel.firePropertyChanged("addsuccess");
    }

    @Override
    public void prepareFailView() {
        viewModel.firePropertyChanged("addfail");
    }

}
