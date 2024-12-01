package com.example.csc207courseproject.use_case.login;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.json.JSONException;

/**
 * Interactor for the Login Use Case.
 */
public class LoginInteractor implements LoginInputBoundary, PropertyChangeListener {
    private final LoginOAuthDataAccessInterface loginOAuthDataAccessObject;
    private final LoginOutputBoundary loginPresenter;
    private final LoginDataAccessInterface loginDataAccessObject;

    /**
     * The class constructor.
     *
     * @param loginOAuthDataAccessInterface the DAO to set for loginOAuthDataAccessObject.
     * @param loginOutputBoundary           the presenter to set for loginPresenter
     * @param loginDataAccessInterface      the DAO to set for loginDataAccessObject
     */
    public LoginInteractor(LoginOAuthDataAccessInterface loginOAuthDataAccessInterface,
                           LoginOutputBoundary loginOutputBoundary,
                           LoginDataAccessInterface loginDataAccessInterface) {
        this.loginOAuthDataAccessObject = loginOAuthDataAccessInterface;
        this.loginPresenter = loginOutputBoundary;
        this.loginDataAccessObject = loginDataAccessInterface;

        loginOAuthDataAccessObject.addListener(this);
    }

    /**
     * Executes the Login Use Case.
     * @return the browser URL where the user can log in
     */
    @Override
    public String execute() {
        try {
            return loginOAuthDataAccessObject.getAuthUrl();
        }
        catch (RuntimeException evt) {
            loginPresenter.prepareFailView();
            return null;
        }
    }

    /**
     * Listens for when the local server receives the auth code, and then gets the user's access token.
     * @param evt the event fired by the local server when it receives the auth code
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final String token;
        try {
            token = loginOAuthDataAccessObject.getToken();
            if (token == null) {
                loginPresenter.prepareFailView();
            }
            loginDataAccessObject.setToken(token);
            final LoginOutputData loginOutputData =
                    new LoginOutputData(loginDataAccessObject.getTournaments());
            loginPresenter.prepareSuccessView(loginOutputData);
        }
        catch (JSONException | InterruptedException event) {
            loginPresenter.prepareFailView();
        }
        loginOAuthDataAccessObject.stopServer();
    }
}
