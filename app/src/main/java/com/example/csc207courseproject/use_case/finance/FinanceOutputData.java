package com.example.csc207courseproject.use_case.finance;

import com.example.csc207courseproject.entities.Participant;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FinanceOutputData {
   // refactor so that theyre private and have a getter method - used in financefragment
    public List<String> participantDisplayList = new ArrayList<>();
    public String playerInfo;
    public String playerID;


    public FinanceOutputData(String playerInfo) {
        this.playerInfo = playerInfo;
    }

    public FinanceOutputData(int playerID) {
        this.playerID = String.valueOf(playerID);
    }


    public FinanceOutputData(Map<Integer, Participant> participantPaymentStatus, int playerID) {
        // need the updated player here as well
        this.participantDisplayList = updateFinancialEntries(participantPaymentStatus);
        this.playerID = String.valueOf(playerID);
    }

    private List<String> updateFinancialEntries(Map<Integer, Participant> participantPaymentStatus) {
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
    }
}
