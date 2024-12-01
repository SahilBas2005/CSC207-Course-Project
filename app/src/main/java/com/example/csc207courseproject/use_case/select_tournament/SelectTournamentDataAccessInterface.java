package com.example.csc207courseproject.use_case.select_tournament;

import java.util.List;

import org.json.JSONException;

/**
 * DAI for the Select Tournament Use Case.
 */
public interface SelectTournamentDataAccessInterface {
    /**
     * Gets a list of tournaments that the user is organizing or an admin of.
     * @return a list containing tournament names (index 0) and ids (index 1)
     * @throws JSONException if there is a problem with the JSON API
     */
    List<List> getTournaments() throws JSONException;

    /**
     * Store the user's access token in the DAO.
     * @param token the user's access token
     */
    void setToken(String token);
}
