package com.example.csc207courseproject.use_case.login;

import java.util.List;

/**
 * The Login Interactor
 */
public class LoginInteractor implements LoginInputBoundary {
    private final LoginDataAccessInterface loginDataAccessObject;
    private final LoginOutputBoundary loginPresenter;

    public LoginInteractor(LoginDataAccessInterface loginDataAccessInterface,
                           LoginOutputBoundary loginOutputBoundary) {
        this.loginDataAccessObject = loginDataAccessInterface;
        this.loginPresenter = loginOutputBoundary;
    }

    @Override
    public void execute() {
        if (loginDataAccessObject.login()) {
            List tournaments = loginDataAccessObject.getTournaments();
            System.out.println(tournaments);        // temporary
            // create a User entity and store tournaments under it?
            // make tournament an entity
            loginPresenter.prepareSuccessView();
        }
        else {
            loginPresenter.prepareFailView("Failed to login!");
        }
    }
}
