package com.example.csc207courseproject.use_case.login;

/**
 * The output boundary for the Login Use Case.
 */
public interface LoginOutputBoundary {

    /**
     * Prepares the success view for the Login Use Case.
     */
    void prepareSuccessView(LoginOutputData loginOutputData);

    /**
     * Prepares the failure view for the Login Use Case.
     */
    void prepareFailView();
}
