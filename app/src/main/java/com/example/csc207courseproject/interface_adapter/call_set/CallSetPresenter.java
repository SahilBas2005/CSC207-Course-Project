package com.example.csc207courseproject.interface_adapter.call_set;

import com.example.csc207courseproject.ui.call.CallViewModel;
import com.example.csc207courseproject.use_case.call_set.CallSetOutputBoundary;
import com.example.csc207courseproject.use_case.call_set.CallSetOutputData;

public class CallSetPresenter implements CallSetOutputBoundary {

    private final CallViewModel viewModel;

    public CallSetPresenter(CallViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(CallSetOutputData outputData) {
        CallSetState currentState = viewModel.getState();
        currentState.addCalledSetID(currentState.getCurrentSet().getSetID());
        outputData.getStation().setOccupied(true);
        viewModel.firePropertyChanged("callsuccess");
    }

    @Override
    public void prepareFailView() {
        viewModel.firePropertyChanged("callfail");
    }

}
