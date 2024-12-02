package com.example.csc207courseproject.interface_adapter.finance;

import android.content.Context;
import com.example.csc207courseproject.use_case.finance.*;

import java.io.File;
import java.util.List;

/**
 * The controller for the Finance Use Case.
 */
public class FinanceController {

    private final FinanceInputBoundary inputBoundary;

    public FinanceController() {
        inputBoundary = new FinanceInteractor();
    }

    public List<String> initializeFinancialEntries(int eventID) {
        FinanceInputData inputData = new FinanceInputData(eventID);
        return inputBoundary.getAllFinanceEntries(inputData);
    }

    public void modifyPartipantPaymentStatus(String cashAmount, String eTransferAmount, String participantString) {
        FinanceInputData inputData = new FinanceInputData(safeParseInt(cashAmount), safeParseInt(eTransferAmount), safeParseInt(extractUntilFirstComma(participantString)));
        inputBoundary.modifyPaymentStatus(inputData);
    }

    public void exportParticipantPaymentStatus(Context context) {
        String fileName = "participant_payment_status.csv";
        File file = new File(context.getFilesDir(), fileName);
        FinanceInputData inputData = new FinanceInputData(file);
        inputBoundary.exportToCSV(inputData);
    }

    private int safeParseInt(String input) {
        try {
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            return 0; // Return default value for invalid inputs
        }
    }

    private static String extractUntilFirstComma(String input) {
        if (input == null || input.isEmpty()) {
            return ""; // Handle null or empty string
        }

        int commaIndex = input.indexOf(',');
        if (commaIndex == -1) {
            return input; // No comma found, return the entire string
        }

        return input.substring(0, commaIndex); // Extract until the first comma
    }

}
