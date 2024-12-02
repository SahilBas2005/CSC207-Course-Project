package com.example.csc207courseproject.use_case.finance;

import java.io.File;

public class FinanceInputData {
    public int eventID;

    public int cashAmount;
    public int eTransferAmount;
    public int participantID;
    public File file;

    public FinanceInputData(int eventID) {
        this.eventID = eventID;
    }

    public FinanceInputData(int cashAmount, int eTransferAmount, int participantID) {
        this.cashAmount = cashAmount;
        this.eTransferAmount = eTransferAmount;
        this.participantID = participantID;
    }

    public FinanceInputData(File file) {
        this.file = file;
    }


}
