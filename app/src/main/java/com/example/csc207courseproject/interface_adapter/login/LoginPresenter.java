package com.example.csc207courseproject.interface_adapter.login;

import com.example.csc207courseproject.interface_adapter.select_tournament.SelectTournamentViewModel;
import com.example.csc207courseproject.interface_adapter.select_tournament.TournamentState;
import com.example.csc207courseproject.use_case.login.LoginOutputBoundary;
import com.example.csc207courseproject.use_case.login.LoginOutputData;

/**
 * Presenter for the Login Use Case.
 */
public class LoginPresenter implements LoginOutputBoundary {

    private final LoginViewModel loginViewModel;
    private final SelectTournamentViewModel selectTournamentViewModel;

    /**
     * The class constructor.
     * @param loginViewModel the view model to set for loginViewModel
     * @param selectTournamentViewModel the view model to set for selectTournamentViewModel
     */
    public LoginPresenter(LoginViewModel loginViewModel,
                          SelectTournamentViewModel selectTournamentViewModel) {
        this.loginViewModel = loginViewModel;
        this.selectTournamentViewModel = selectTournamentViewModel;
    }

    /**
     * Prepares a view for when the use case is executed successfully.
     * @param loginOutputData necessary data to prepare the view
     */
    @Override
    public void prepareSuccessView(LoginOutputData loginOutputData) {
        final TournamentState tournamentState = selectTournamentViewModel.getState();
        tournamentState.setTournamentNames(loginOutputData.getTournamentNames());
        tournamentState.setTournamentIds(loginOutputData.getTournamentIds());
        loginViewModel.firePropertyChanged("loginsuccess");

    }

    /**
     * Prepares a view for when the use case fails.
     */
    @Override
    public void prepareFailView() {
        loginViewModel.firePropertyChanged("loginfail");
    }
}
