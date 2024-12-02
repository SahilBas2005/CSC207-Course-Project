package com.example.csc207courseproject.data_access.OAuth;

import com.example.csc207courseproject.data_access.DataAccessException;

public class OAuthException extends DataAccessException {
    public OAuthException(String errorMessage) {
        super(errorMessage);
    }
}
