package com.example.csc207courseproject.interface_adapter.login;

import com.example.csc207courseproject.interface_adapter.ViewManagerModel;
import com.example.csc207courseproject.interface_adapter.select_tournament.SelectTournamentViewModel;
import com.example.csc207courseproject.interface_adapter.select_tournament.TournamentState;
import com.example.csc207courseproject.use_case.login.LoginOutputBoundary;
import com.example.csc207courseproject.use_case.login.LoginOutputData;

public class LoginPresenter implements LoginOutputBoundary {

    private final LoginViewModel loginViewModel;
    private final SelectTournamentViewModel selectTournamentViewModel;

    public LoginPresenter(LoginViewModel loginViewModel,
                          SelectTournamentViewModel selectTournamentViewModel) {
        this.loginViewModel = loginViewModel;
        this.selectTournamentViewModel = selectTournamentViewModel;
    }

    @Override
    public void prepareSuccessView(LoginOutputData loginOutputData) {
        final TournamentState tournamentState = selectTournamentViewModel.getState();
        tournamentState.setTournamentNames(loginOutputData.getTournamentNames());
        tournamentState.setTournamentIds(loginOutputData.getTournamentIds());
        loginViewModel.firePropertyChanged("loginsuccess");

    }

    @Override
    public void prepareFailView() {
        loginViewModel.firePropertyChanged("loginfail");
    }
}
