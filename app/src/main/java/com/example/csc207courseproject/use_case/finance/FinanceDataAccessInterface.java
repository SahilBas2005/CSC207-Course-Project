package com.example.csc207courseproject.use_case.finance;

import com.example.csc207courseproject.entities.Participant;

import java.util.Map;

public interface FinanceDataAccessInterface {

    /**
     * Gets all Participant Payment Status for a given event.
     * @param eventID The ID of the event
     * @return A map of all the phase IDs mapped to a participant entity
     */
    Map<Integer, Participant> fetchParticipantPaymentStatus(int eventID);
    void modifyParticipantPaymentStatus(int participantId);
    Map<Integer, Participant> getParticipantPaymentStatus();
}
