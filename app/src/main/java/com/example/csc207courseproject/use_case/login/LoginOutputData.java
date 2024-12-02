package com.example.csc207courseproject.use_case.login;

import java.util.List;

/**
 * Output Data for the Login Use Case.
 */
public class LoginOutputData {

    private final List<String> tournamentNames;
    private final List<Integer> tournamentIds;

    /**
     * The class constructor.
     * @param tournaments a list containing tournament names (index 0) and ids (index 1)
     */
    public LoginOutputData(List<List> tournaments) {
        tournamentNames = tournaments.get(0);
        tournamentIds = tournaments.get(1);
    }

    /**
     * Gets the list of tournament names that the user is organizing or is an admin of.
     * @return a list of tournament names
     */
    public List<String> getTournamentNames() {
        return tournamentNames;
    }

    /**
     * Gets the list of tournament ids that the user is organizing or is an admin of.
     * @return a list of tournament ids.
     */
    public List<Integer> getTournamentIds() {
        return tournamentIds;
    }
}
