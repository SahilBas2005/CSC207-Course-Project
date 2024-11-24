package com.example.csc207courseproject.interface_adapter.login;

/**
 * The state for the Login View Model.
 */
public class LoginState {
    private String error = "";

    public void setError(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
