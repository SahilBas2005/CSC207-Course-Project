package com.example.csc207courseproject.interface_adapter.get_stations;

import com.example.csc207courseproject.interface_adapter.call_set.CallSetState;
import com.example.csc207courseproject.ui.call.CallViewModel;
import com.example.csc207courseproject.use_case.get_stations.GetStationsOutputBoundary;
import com.example.csc207courseproject.use_case.get_stations.GetStationsOutputData;

public class GetStationsPresenter implements GetStationsOutputBoundary {

    private final CallViewModel viewModel;

    public GetStationsPresenter(CallViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(GetStationsOutputData outputData) {
        final CallSetState setState = viewModel.getState();
        setState.setStations(outputData.getStations());
        viewModel.firePropertyChanged("getstationssuccess");
    }

    @Override
    public void prepareFailView(String errorMessage) {
        viewModel.firePropertyChanged("getstationsfail");
    }

}
