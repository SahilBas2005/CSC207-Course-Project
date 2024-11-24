package com.example.csc207courseproject.use_case.login;

import java.util.List;

/**
 * Output Data for the Login Use Case.
 */
public class LoginOutputData {

    private final String userID;
    private final String userName;
    private final List tournaments;

    public LoginOutputData(String userID, String userName, List tournaments) {
        this.userID = userID;
        this.userName = userName;
        this.tournaments = tournaments;
    }

    public String getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }

    public List getTournaments() {
        return tournaments;
    }
}
