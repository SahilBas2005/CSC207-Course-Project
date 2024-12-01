package com.example.csc207courseproject.use_case.login;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.json.JSONException;

import com.example.csc207courseproject.use_case.select_tournament.SelectTournamentDataAccessInterface;

/**
 * Interactor for the Login Use Case.
 */
public class LoginInteractor implements LoginInputBoundary, PropertyChangeListener {
    private final LoginDataAccessInterface loginDataAccessObject;
    private final LoginOutputBoundary loginPresenter;
    private final SelectTournamentDataAccessInterface selectTournamentDataAccessObject;

    /**
     * The class constructor.
     * @param loginDataAccessInterface the DAO to set for loginDataAccessObject.
     * @param loginOutputBoundary the presenter to set for loginPresenter
     * @param selectTournamentDataAccessInterface the DAO to set for selectTournamentDataAccessObject
     */
    public LoginInteractor(LoginDataAccessInterface loginDataAccessInterface,
                           LoginOutputBoundary loginOutputBoundary,
                           SelectTournamentDataAccessInterface selectTournamentDataAccessInterface) {
        this.loginDataAccessObject = loginDataAccessInterface;
        this.loginPresenter = loginOutputBoundary;
        this.selectTournamentDataAccessObject = selectTournamentDataAccessInterface;

        loginDataAccessObject.addListener(this);
    }

    /**
     * Executes the Login Use Case.
     * @return the browser URL where the user can log in
     */
    @Override
    public String execute() {
        try {
            return loginDataAccessObject.getAuthUrl();
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
            token = loginDataAccessObject.getToken();
            if (token == null) {
                loginPresenter.prepareFailView();
            }
            selectTournamentDataAccessObject.setToken(token);
            final LoginOutputData loginOutputData =
                    new LoginOutputData(selectTournamentDataAccessObject.getTournaments());
            loginPresenter.prepareSuccessView(loginOutputData);
        }
        catch (JSONException | InterruptedException event) {
            loginPresenter.prepareFailView();
        }
        loginDataAccessObject.stopServer();
    }
}
