package com.example.csc207courseproject.ui.finance;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.example.csc207courseproject.data_access.APIDataAccessObject;
import com.example.csc207courseproject.entities.Participant;


public class FinanceViewModel extends ViewModel {

    private final MutableLiveData<List<String>> financialEntries;

    public FinanceViewModel() {
        int fakeGameID = 1257516;
        APIDataAccessObject apiDataAccessObject = new APIDataAccessObject();
        HashMap<Integer, Participant> participantPaymentStatus = apiDataAccessObject.getParticipantPaymentStatus( fakeGameID,"tournament/skipping-classes-world-championship-start-gg-api-test");
        financialEntries = new MutableLiveData<>();
        // Initialize with some default values
        List<String> defaultEntries = new ArrayList<>();
        for (Participant participant : participantPaymentStatus.values()) {
            String entry = String.format(
                    "%d, %s, Status: %s",
                    participant.getParticipantId(),
                    participant.getName(),
                    participant.isPaid() ? "Paid" : "Unpaid"
            );
            defaultEntries.add(entry);
        }
        financialEntries.setValue(defaultEntries);
    }

    public LiveData<List<String>> getFinancialEntries() {
        return financialEntries;
    }

    public void updateFinancialEntries(List<String> newEntries) {
        financialEntries.setValue(newEntries);
    }
}
