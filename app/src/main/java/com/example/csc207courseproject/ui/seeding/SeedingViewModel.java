package com.example.csc207courseproject.ui.seeding;

import com.example.csc207courseproject.interface_adapter.ViewModel;
import com.example.csc207courseproject.interface_adapter.update_seeding.SeedingState;

public class SeedingViewModel extends ViewModel<SeedingState> {

    public SeedingViewModel() {
        super("seeding");
        setState(new SeedingState());
    }

}