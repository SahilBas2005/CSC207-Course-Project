package com.example.csc207courseproject.interface_adapter.decline_set;

import com.example.csc207courseproject.ui.call.CallViewModel;
import com.example.csc207courseproject.use_case.decline_set.DeclineSetOutputBoundary;

public class DeclineSetPresenter implements DeclineSetOutputBoundary {

    private final CallViewModel viewModel;

    public DeclineSetPresenter(CallViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView() {
        viewModel.firePropertyChanged("declinesuccess");
    }
}
