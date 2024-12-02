package com.example.csc207courseproject.ui.finance;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import com.example.csc207courseproject.interface_adapter.finance.FinanceController;
import com.example.csc207courseproject.use_case.finance.FinanceInputData;
import com.example.csc207courseproject.use_case.finance.FinanceOutputBoundary;
import com.example.csc207courseproject.use_case.finance.FinancePresenter;

// VIEW MODEL
public class FinanceViewModel extends ViewModel {

    private final MutableLiveData<List<String>> financialEntries = new MutableLiveData<>();
    private final FinanceController controller;
    FinanceOutputBoundary outputBoundary;

    public FinanceViewModel() {
        // call the controller to fetch the data
        int fakeGameID = 1257516;
        outputBoundary = new FinancePresenter()
        controller = new FinanceController();
        List<String> initialEntries = controller.initializeFinancialEntries(fakeGameID);
        financialEntries.setValue(initialEntries);
    }

    public LiveData<List<String>> getFinancialEntries() {
        return financialEntries;
    }

    public void modifyParticipantPaymentStatus(String cashAmount, String eTransferAmount, String playerInfo) {
        controller.modifyPartipantPaymentStatus(cashAmount, eTransferAmount, playerInfo);
    }

    public void exportParticipantPaymentData(Context context) {
        controller.exportParticipantPaymentStatus(context);
    }

    public void updateFinancialEntries(List<String> updatedEntries) {
        financialEntries.setValue(updatedEntries);
    }
}
