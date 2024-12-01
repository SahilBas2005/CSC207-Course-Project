package com.example.csc207courseproject.interface_adapter.select_tournament;

import java.util.List;
import java.util.Map;

/**
 * A state containing the tournaments to be displayed to the user in the Select Tournament view.
 */
public class TournamentState {
    private List<String> tournamentNames;
    private List<Integer> tournamentIds;

    public List<String> getTournamentNames() {
        return tournamentNames;
    }

    public void setTournamentNames(List<String> tournamentNames) {
        this.tournamentNames = tournamentNames;
    }

    public List<Integer> getTournamentIds() {
        return tournamentIds;
    }

    public void setTournamentIds(List<Integer> tournamentIds) {
        this.tournamentIds = tournamentIds;
    }
}
