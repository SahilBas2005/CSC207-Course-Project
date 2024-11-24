package com.example.csc207courseproject.interface_adapter.login;

import com.example.csc207courseproject.interface_adapter.ViewModel;

public class LoginViewModel extends ViewModel<LoginState> {

    public static final String TITLE_LABEL = "Start.gg TO Login";
    public static final String LOGIN_BUTTON_LABEL = "Log in";

    public LoginViewModel() {
        super("log in");
        setState(new LoginState());
    }

}
