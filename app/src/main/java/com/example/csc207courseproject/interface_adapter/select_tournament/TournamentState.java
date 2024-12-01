package com.example.csc207courseproject.interface_adapter.select_tournament;

import java.util.List;

/**
 * A state containing the tournaments to be displayed to the user in the Select Tournament view.
 */
public class TournamentState {
    private List<String> tournamentNames;
    private List<Integer> tournamentIds;

    /**
     * Gets the names of all tournaments that the user is an organizer or admin of.
     * @return a list of tournament names
     */
    public List<String> getTournamentNames() {
        return tournamentNames;
    }

    /**
     * Sets the names of all tournaments that the user is an organizer or admin of.
     * @param tournamentNames the names of the tournaments
     */
    public void setTournamentNames(List<String> tournamentNames) {
        this.tournamentNames = tournamentNames;
    }

    /**
     * Gets the ids of all tournaments that the user is an organizer or admin of.
     * @return a list of tournament ids
     */
    public List<Integer> getTournamentIds() {
        return tournamentIds;
    }

    /**
     * Sets the ids of all tournaments that the user is an organizer or admin of.
     * @param tournamentIds the ids of the tournaments
     */
    public void setTournamentIds(List<Integer> tournamentIds) {
        this.tournamentIds = tournamentIds;
    }
}
