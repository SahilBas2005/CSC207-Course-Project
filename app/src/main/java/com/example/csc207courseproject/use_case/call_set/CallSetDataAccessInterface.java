package com.example.csc207courseproject.use_case.call_set;

/**
 * The DAO for the call set use case.
 */
public interface CallSetDataAccessInterface {

    /**
     * Marks the parameter set as in progress
     * @param setId Set id of the set
     */
    public void callSet(int setId);
}
