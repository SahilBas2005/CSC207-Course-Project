package com.example.csc207courseproject.use_case.login;

import java.util.List;
import java.util.Map;

/**
 * Output Data for the Login Use Case.
 */
public class LoginOutputData {

    private final List<String> tournamentNames;
    private final List<Integer> tournamentIds;

    public LoginOutputData(List<List> tournaments) {
        tournamentNames = tournaments.get(0);
        tournamentIds = tournaments.get(1);
    }

    public List<String> getTournamentNames() {
        return tournamentNames;
    }

    public List<Integer> getTournamentIds() {
        return tournamentIds;
    }
}
