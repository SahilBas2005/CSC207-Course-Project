package com.example.csc207courseproject.interface_adapter.select_event;

import com.example.csc207courseproject.use_case.select_event.SelectEventOutputBoundary;

/**
 * Presenter for the Select Event Use Case.
 */
public class SelectEventPresenter implements SelectEventOutputBoundary {

    private final SelectEventViewModel selectEventViewModel;

    /**
     * The class constructor.
     * @param selectEventViewModel the view model to set for selectEventViewModel
     */
    public SelectEventPresenter(SelectEventViewModel selectEventViewModel) {
        this.selectEventViewModel = selectEventViewModel;
    }

    /**
     * Prepares a view for when the use case is executed successfully.
     */
    @Override
    public void prepareSuccessView() {
        selectEventViewModel.firePropertyChanged("eventsuccess");
    }

    /**
     * Prepares a view for when the use case fails.
     */
    @Override
    public void prepareFailView() {
        selectEventViewModel.firePropertyChanged("eventfail");
    }
}
