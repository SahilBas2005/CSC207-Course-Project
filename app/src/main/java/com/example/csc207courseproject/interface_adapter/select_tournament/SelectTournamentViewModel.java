package com.example.csc207courseproject.interface_adapter.select_tournament;

import com.example.csc207courseproject.interface_adapter.ViewModel;

public class SelectTournamentViewModel extends ViewModel<TournamentState> {

    public SelectTournamentViewModel() {
        super("select tournament");
        setState(new TournamentState());
    }
}
