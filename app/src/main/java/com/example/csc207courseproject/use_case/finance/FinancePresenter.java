package com.example.csc207courseproject.use_case.finance;

import android.content.Context;
import android.widget.Toast;
import com.example.csc207courseproject.ui.finance.FinanceViewModel;

public class FinancePresenter implements FinanceOutputBoundary {
    private static FinanceViewModel financeViewModel;
    private static Context mContext;

    public FinancePresenter(Context context, FinanceViewModel viewModel) {
        financeViewModel = viewModel;
        mContext = context;
    }

    private void showToast(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateStateAndShowToast(FinanceOutputData outputData) {
        financeViewModel.updateFinancialEntries(outputData.participantDisplayList);
    }

    @Override
    public void showSuccessToast(FinanceOutputData outputData) {
        showToast(String.format("Player %s payment status was marked as paid.", outputData.playerID));
    }


    @Override
    public void showFailureToast(FinanceOutputData outputData) {
        showToast(String.format("Could not change Player %s payment status.", outputData.playerID));
    }

    @Override
    public void showExportSuccessToast() {
        showToast("Exported data successfully.");
    }

    @Override
    public void showExportFailureToast() {
        showToast("Failure to export data to csv.");
    }
}
