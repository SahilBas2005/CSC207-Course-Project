package com.example.csc207courseproject.ui.finance;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class FinanceViewModel extends ViewModel {

    private final MutableLiveData<List<String>> financialEntries;

    public FinanceViewModel() {
        financialEntries = new MutableLiveData<>();
        // Initialize with some default values
        List<String> defaultEntries = new ArrayList<>();
        defaultEntries.add("Player A - $50");
        defaultEntries.add("Player B - $30");
        defaultEntries.add("Player C - $20");
        financialEntries.setValue(defaultEntries);
    }

    public LiveData<List<String>> getFinancialEntries() {
        return financialEntries;
    }

    public void updateFinancialEntries(List<String> newEntries) {
        financialEntries.setValue(newEntries);
    }
}
