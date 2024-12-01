package com.example.csc207courseproject.interface_adapter.call_set;

import com.example.csc207courseproject.ui.call.CallViewModel;
import com.example.csc207courseproject.use_case.call_set.CallSetOutputBoundary;
import com.example.csc207courseproject.use_case.call_set.CallSetOutputData;

import java.util.List;

public class CallSetPresenter implements CallSetOutputBoundary {

    private final CallViewModel viewModel;

    public CallSetPresenter(CallViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(CallSetOutputData outputData) {
        CallSetState currentState = viewModel.getState();
        int setId = currentState.getCurrentSet().getSetID();
        List<Integer> calledSetIDs = currentState.getCalledSetIDs();
        if (calledSetIDs.size() == 10) {
                calledSetIDs.remove(0);
        }
        calledSetIDs.add(setId);
        outputData.getStation().setOccupied(true);
        viewModel.firePropertyChanged("callsuccess");
    }

    @Override
    public void prepareFailView() {
        viewModel.firePropertyChanged("callfail");
    }

}
