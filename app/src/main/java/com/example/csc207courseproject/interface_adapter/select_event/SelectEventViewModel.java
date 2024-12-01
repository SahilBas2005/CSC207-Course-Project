package com.example.csc207courseproject.interface_adapter.select_event;

import com.example.csc207courseproject.interface_adapter.ViewModel;

public class SelectEventViewModel extends ViewModel<EventState> {

    public SelectEventViewModel() {
        super("select_event");
        setState(new EventState());
    }
}
