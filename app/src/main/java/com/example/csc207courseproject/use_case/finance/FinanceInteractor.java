package com.example.csc207courseproject.use_case.finance;

import com.example.csc207courseproject.data_access.APIDataAccessObject;
import com.example.csc207courseproject.entities.Participant;
import com.example.csc207courseproject.ui.finance.FinanceFragment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.util.*;

public class FinanceInteractor implements FinanceInputBoundary {
    FinanceDataAccessInterface dataAccess = new APIDataAccessObject();
    FinanceOutputBoundary outputBoundary;

    public FinanceInteractor() {}

    @Override
    public List<String> getAllFinanceEntries(FinanceInputData inputData) {
        return convertMapToList(dataAccess.fetchParticipantPaymentStatus(inputData.eventID));
    }

    @Override
    public void modifyPaymentStatus(FinanceInputData inputData) {
        try {
            if (inputData.eTransferAmount > 0 || inputData.cashAmount > 0) {
                dataAccess.modifyParticipantPaymentStatus(inputData.participantID);
                Map<Integer, Participant> updatedPaymentStatuses = dataAccess.getParticipantPaymentStatus();
                FinanceOutputData outputData = new FinanceOutputData(updatedPaymentStatuses, inputData.participantID);

                // update the view
                outputBoundary.updateStateAndShowToast(outputData);

                // pass it back to the presenter
                outputBoundary.showSuccessToast(outputData);
            }
        } catch (Exception e) {
            FinanceOutputData outputData = new FinanceOutputData(inputData.participantID);
            outputBoundary.showFailureToast(outputData);
        }
    }

    @Override
    public void exportToCSV(FinanceInputData inputData) {
        File file = inputData.file;
        Map<Integer, Participant> participantPaymentStatus = dataAccess.getParticipantPaymentStatus();
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

            outputBoundary.showExportSuccessToast();
        } catch (IOException e) {
            outputBoundary.showExportFailureToast();
        }
    }


    private List<String> convertMapToList(Map<Integer, Participant> entries) {
        List<String> defaultEntries = new ArrayList<>();
        for (Participant participant : entries.values()) {
            String entry = String.format(
                    "%d, %s, Status: %s",
                    participant.getParticipantId(),
                    participant.getName(),
                    participant.isPaid() ? "Paid" : "Unpaid"
            );
            defaultEntries.add(entry);
        }
        return defaultEntries;
    }

}
