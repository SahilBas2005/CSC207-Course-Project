package com.example.csc207courseproject.use_case.login;

/**
 * The output boundary for the Login Use Case.
 */
public interface LoginOutputBoundary {

    /**
     * Prepares a view for when the use case is executed successfully.
     * @param loginOutputData necessary data to prepare the view
     */
    void prepareSuccessView(LoginOutputData loginOutputData);

    /**
     * Prepares a view for when the use case fails.
     */
    void prepareFailView();
}
