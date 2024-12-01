package com.example.csc207courseproject.use_case.login;

import android.content.Context;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import com.example.csc207courseproject.data_access.APIDataAccessObject;
import com.example.csc207courseproject.use_case.select_tournament.SelectTournamentDataAccessInterface;
import org.json.JSONException;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Map;

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
    public void execute(AppCompatActivity activity) {
        try {
            loginDataAccessObject.getAuthCode(activity);
        }
        catch(RuntimeException e) {
            loginPresenter.prepareFailView();
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
