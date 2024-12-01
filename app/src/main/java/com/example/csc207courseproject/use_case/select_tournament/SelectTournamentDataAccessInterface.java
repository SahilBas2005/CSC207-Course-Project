package com.example.csc207courseproject.use_case.select_tournament;

import org.json.JSONException;

import java.util.List;

public interface SelectTournamentDataAccessInterface {
    /**
     * Gets a list of tournaments organized by the user.
     * @return a map of tournaments by ids
     */
    List<List> getTournaments() throws JSONException;

    /**
     * Store the user's access token in the DAO.
     */
    void setToken(String token);
}
