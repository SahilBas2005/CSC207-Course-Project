package com.example.csc207courseproject.interface_adapter.login;

import com.example.csc207courseproject.use_case.login.LoginInputBoundary;

/**
 * Controller for the Login Use Case.
 */
public class LoginController {

    private final LoginInputBoundary loginInteractor;

    /**
     * The class constructor.
     * @param loginInteractor the interactor to set for loginInteractor
     */
    public LoginController(LoginInputBoundary loginInteractor) {
        this.loginInteractor = loginInteractor;
    }

    /**
     * Executes the Login Use Case.
     * @return the browser URL where the user can log in
     */
    public String execute() {
        return loginInteractor.execute();
    }

}
