package com.example.csc207courseproject.use_case.login;

/**
 * Input Boundary for the Login Use Case.
 */
public interface LoginInputBoundary {

    /**
     * Executes the login use case.
     * @return the browser URL where the user can log in
     */
    String execute();

}
