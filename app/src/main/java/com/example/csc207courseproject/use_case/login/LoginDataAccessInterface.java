package com.example.csc207courseproject.use_case.login;

import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;

import java.beans.PropertyChangeListener;
import java.util.List;

/**
 * DAO for the Login Use Case
 */
public interface LoginDataAccessInterface {

    /**
     * Start the server where the auth code will be sent once the user logs in, and prompt the user to log in.
     */
    void getAuthCode(AppCompatActivity appCompatActivity);

    /**
     * Add a listener to listen in on when the auth code is received.
     */
    void addListener(PropertyChangeListener listener);

    /**
     * Get the user's access token by using the auth code.
     */
    String getToken() throws InterruptedException;

    /**
     * Stop the HTTP Server
     */
    void stopServer();

}
