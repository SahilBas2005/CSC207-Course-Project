package com.example.csc207courseproject.use_case.finance;

import com.example.csc207courseproject.data_access.APIDataAccessObject;
import com.example.csc207courseproject.entities.Participant;
import android.content.Context;
import com.example.csc207courseproject.interface_adapter.finance.FinancePresenter;
import com.example.csc207courseproject.ui.finance.FinanceViewModel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import java.util.*;
public class FinanceInteractor implements FinanceInputBoundary {
    FinanceDataAccessInterface dataAccess = new APIDataAccessObject();
    FinanceOutputBoundary outputBoundary = new FinancePresenter();
    FinanceOutputData outputData = new FinanceOutputData();
    @Override
    public void execute(FinanceInputData financeInputData) {
        // need ti access the data here
//        dataAccessObject.getParticipantPaymentStatus()
        // need to send the updated data to the output data (through the output data)
    }

    public void exportToCSV(Context context, Map<Integer, Participant> participantPaymentStatus) {
        // Define the file name
        String fileName = "participant_payment_status.csv";

        // Get the file path for internal storage
        File file = new File(context.getFilesDir(), fileName);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            // Write the header of the CSV file
            writer.write("Sponsor,Name,Status");
            writer.newLine();

            // Iterate through the participantPaymentStatus HashMap and write data to the CSV file
            for (Participant participant : participantPaymentStatus.values()) {
                String sponsor = participant.getSponsor();
                String name = participant.getName();
                String status = participant.isPaid() ? "Paid" : "Unpaid";

                writer.write(sponsor + "," + name + "," + status);
                writer.newLine();
            }

            System.out.println("File written to: " + file.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    public int safeParseInt(String input) {
        try {
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            return 0; // Return default value for invalid inputs
        }
    }

    public static String extractUntilFirstComma(String input) {
        if (input == null || input.isEmpty()) {
            return ""; // Handle null or empty string
        }

        int commaIndex = input.indexOf(',');
        if (commaIndex == -1) {
            return input; // No comma found, return the entire string
        }

        return input.substring(0, commaIndex); // Extract until the first comma
    }

    public boolean handlePaymentStatusUpdate(String cashAmount, String eTransferAmount, String playerInfo, FinanceViewModel viewModel) {
        if (safeParseInt(cashAmount) > 0 || safeParseInt(eTransferAmount) > 0) {
            String participantID = extractUntilFirstComma(playerInfo);
            int participantIDParsed = Integer.parseInt(participantID);
            dataAccess.modifyParticipantPaymentStatus(participantIDParsed);

            List<String> updatedEntries = outputData.updateFinancialEntries(dataAccess.getParticipantPaymentStatus());
            // call the output boundary, the class that implements the output boundary calls the updateFinancialEntries
//            outputBoundary.
//            Map<Integer, Participant> currentPaymentState = dataAccess.getParticipantPaymentStatus();
//            List<String> packagedData = new FinanceOutputData().updateFinancialEntries(currentPaymentState);
//            viewModel.updateFinancialEntries(packagedData);
////
//          showToast(String.format("Player %s payment status was marked as paid.", playerInfo));
            return true;
        }
        return false;
    }
}
