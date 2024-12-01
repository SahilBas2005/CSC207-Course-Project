package com.example.csc207courseproject.data_access.api;

import com.example.csc207courseproject.data_access.DataAccessException;

public class APIDataAccessException extends DataAccessException {
    public APIDataAccessException(String errorMessage) {
        super(errorMessage);
    }
}
