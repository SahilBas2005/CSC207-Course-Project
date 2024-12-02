package com.example.csc207courseproject.use_case.tournament_description;

import java.util.List;

public interface TournamentDescriptionDataAccessInterface {

    String generateTournamentDescription(String eventName, int noOfPlayers);
}
