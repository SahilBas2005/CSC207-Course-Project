package com.example.csc207courseproject.interface_adapter.login;

import com.example.csc207courseproject.interface_adapter.ViewModel;

public class LoginViewModel extends ViewModel<LoginState> {

    public LoginViewModel() {
        super("log in");
        setState(new LoginState());
    }

}
