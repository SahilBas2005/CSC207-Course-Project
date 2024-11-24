package com.example.csc207courseproject.interface_adapter.main;

import com.example.csc207courseproject.interface_adapter.ViewModel;

public class MainViewModel extends ViewModel<MainState> {
    public MainViewModel() {
        super("main");
        setState(new MainState());
    }
}
