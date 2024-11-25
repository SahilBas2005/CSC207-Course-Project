package com.example.csc207courseproject.interface_adapter.finance;

import com.example.csc207courseproject.interface_adapter.ViewModel;
import com.example.csc207courseproject.interface_adapter.login.LoginState;

public class FinanaceViewModel extends ViewModel<FinanceState> {
    public static final String LOGIN_BUTTON_LABEL = "Log in";

    public FinanaceViewModel() {
        super("finance");
        setState(new FinanceState());
    }

}
