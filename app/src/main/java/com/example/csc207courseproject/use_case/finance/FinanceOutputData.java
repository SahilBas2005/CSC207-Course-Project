package com.example.csc207courseproject.use_case.finance;

import com.example.csc207courseproject.entities.Participant;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FinanceOutputData {
    List<String> participantDisplayList = new ArrayList<>();

    public List<String> updateFinancialEntries(Map<Integer, Participant> participantPaymentStatus) {
        for (Participant participant : participantPaymentStatus.values()) {
            String entry = String.format(
                    "%d, %s, Status: %s",
                    participant.getParticipantId(),
                    participant.getName(),
                    participant.isPaid() ? "Paid" : "Unpaid"
            );
            participantDisplayList.add(entry);
        }
        return participantDisplayList;

//        financialEntries.setValue(updatedEntries);
    }
}
