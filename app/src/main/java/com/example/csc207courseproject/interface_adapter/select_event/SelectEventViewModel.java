package com.example.csc207courseproject.interface_adapter.select_event;

import com.example.csc207courseproject.interface_adapter.ViewModel;

/**
 * The View Model for the Select Event View.
 */
public class SelectEventViewModel extends ViewModel<EventState> {

    /**
     * The class constructor.
     */
    public SelectEventViewModel() {
        super("select_event");
        setState(new EventState());
    }
}
