package com.example.csc207courseproject.use_case.tournament_description;

public class TournamentDescriptionInputData {

    private final Integer tournamentId;

    public TournamentDescriptionInputData(Integer tournamentId) {
        this.tournamentId = tournamentId;
    }

    public Integer getTournamentId() {
        return tournamentId;
    }
}
