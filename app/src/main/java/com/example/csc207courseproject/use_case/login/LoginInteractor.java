package com.example.csc207courseproject.use_case.login;

import com.example.csc207courseproject.use_case.select_tournament.SelectTournamentDataAccessInterface;
import org.json.JSONException;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * The Login Interactor
 */
public class LoginInteractor implements LoginInputBoundary, PropertyChangeListener {
    private final LoginDataAccessInterface loginDataAccessObject;
    private final LoginOutputBoundary loginPresenter;
    private final SelectTournamentDataAccessInterface selectTournamentDataAccessObject;

    public LoginInteractor(LoginDataAccessInterface loginDataAccessInterface,
                           LoginOutputBoundary loginOutputBoundary,
                           SelectTournamentDataAccessInterface selectTournamentDataAccessInterface) {
        this.loginDataAccessObject = loginDataAccessInterface;
        this.loginPresenter = loginOutputBoundary;
        this.selectTournamentDataAccessObject = selectTournamentDataAccessInterface;

        loginDataAccessObject.addListener(this);
    }

    @Override
    public String execute() {
        try {
            return loginDataAccessObject.getAuthURL();
        }
        catch(RuntimeException e) {
            loginPresenter.prepareFailView();
            return null;
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String token;
        try {
            token = loginDataAccessObject.getToken();
            if (token == null) {
                loginPresenter.prepareFailView();
            }
            selectTournamentDataAccessObject.setTOKEN(token);
            final LoginOutputData loginOutputData = new LoginOutputData(selectTournamentDataAccessObject.getTournaments());
            loginPresenter.prepareSuccessView(loginOutputData);
        }
        catch (JSONException | InterruptedException e) {
            loginPresenter.prepareFailView();
        }
        loginDataAccessObject.stopServer();
    }
}
