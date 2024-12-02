package com.example.csc207courseproject.use_case.finance;

public interface FinanceOutputBoundary {
    void updateStateAndShowToast(FinanceOutputData outputData);

    void showSuccessToast(FinanceOutputData outputData);

    void showFailureToast(FinanceOutputData outputData);

    void showExportSuccessToast();

    void showExportFailureToast();
}
