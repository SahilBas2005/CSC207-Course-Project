package com.example.csc207courseproject.interface_adapter.select_event;

import com.example.csc207courseproject.use_case.select_event.SelectEventOutputBoundary;

public class SelectEventPresenter implements SelectEventOutputBoundary {

    private final SelectEventViewModel selectEventViewModel;

    public SelectEventPresenter(SelectEventViewModel selectEventViewModel) {
        this.selectEventViewModel = selectEventViewModel;
    }

    @Override
    public void prepareSuccessView() {
        selectEventViewModel.firePropertyChanged("eventsuccess");
    }

    @Override
    public void prepareFailView() {
        selectEventViewModel.firePropertyChanged("eventfail");
    }
}
