package com.example.csc207courseproject.interface_adapter.select_tournament;

import com.example.csc207courseproject.interface_adapter.ViewModel;

/**
 * The View Model for the Select Tournament View.
 */
public class SelectTournamentViewModel extends ViewModel<TournamentState> {

    /**
     * The class constructor.
     */
    public SelectTournamentViewModel() {
        super("select tournament");
        setState(new TournamentState());
    }
}
