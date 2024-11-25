package com.example.csc207courseproject.use_case.finance;

import java.util.Map;

public interface FinanceDataAccessInterface {

    /**
     * Gets all Participant Payment Status for a given event.
     * @param eventID The ID of the event
     * @return A map of all the phase IDs mapped to a participant entity
     */
    Map<Integer, Boolean> getParticipantPaymentStatus(int eventID);

}
