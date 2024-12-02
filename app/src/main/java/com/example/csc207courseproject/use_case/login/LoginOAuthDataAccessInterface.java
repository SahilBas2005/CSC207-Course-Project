package com.example.csc207courseproject.use_case.login;

import java.beans.PropertyChangeListener;

/**
 * OAuth DAI for the Login Use Case.
 */
public interface LoginOAuthDataAccessInterface {

    /**
     * Start the server where the auth code will be sent once the user logs in, and prompt the user to log in.
     * @return the browser URL where the user can log in
     */
    String getAuthUrl();

    /**
     * Add a listener to listen in on when the auth code is received.
     * @param listener the listener to add
     */
    void addListener(PropertyChangeListener listener);

    /**
     * Get the user's access token by using the auth code.
     * @return the user's access token
     * @throws InterruptedException if the thread is interrupted before or during the execution of this method
     */
    String getToken() throws InterruptedException;

    /**
     * Stop the HTTP Server.
     */
    void stopServer();

}
