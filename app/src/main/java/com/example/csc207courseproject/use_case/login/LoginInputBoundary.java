package com.example.csc207courseproject.use_case.login;

public interface LoginInputBoundary {

    /**
     * Executes the login use case.
     * @return the browser URL where the user can log in
     */
    String execute();

}
