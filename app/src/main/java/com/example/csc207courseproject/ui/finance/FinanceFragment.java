package com.example.csc207courseproject.ui.finance;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.csc207courseproject.data_access.APIDataAccessObject;
import com.example.csc207courseproject.databinding.FragmentFinanceBinding;
import com.example.csc207courseproject.databinding.PlayerFinancePopupBinding;
import com.example.csc207courseproject.entities.Participant;
import com.example.csc207courseproject.use_case.finance.FinanceInteractor;
import com.example.csc207courseproject.use_case.finance.FinanceOutputData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FinanceFragment extends Fragment {

    private FragmentFinanceBinding binding;
    private FinanceViewModel financeViewModel;
    private Context mContext;
    public FinanceInteractor interactor = new FinanceInteractor();
    public APIDataAccessObject dataAccess = new APIDataAccessObject();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Set up ViewModel
        financeViewModel = new ViewModelProvider(this).get(FinanceViewModel.class);

        // Set up binding
        binding = FragmentFinanceBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Observe and display the list
        observeFinancialEntries();

        // NEED TO REFACTOR THIS ITNO A VIEW MODEL OBJECT, THEN UPDATE IT IN THE PRESENTER
        binding.exportButton.setOnClickListener(v -> {
            Map<Integer, Participant> participantPaymentStatus = dataAccess.getParticipantPaymentStatus();

            if (participantPaymentStatus == null || participantPaymentStatus.isEmpty()) {
                showToast("No participant payment data to export.");
            } else {
                // Call the export method and pass the context
                interactor.exportToCSV(requireContext(), participantPaymentStatus);
                showToast("Participant payment data exported successfully!");
            }
        });

        return root;
    }






    private void observeFinancialEntries() {
        ListView financialListView = binding.seedsList;

        // Observe LiveData from the ViewModel
        financeViewModel.getFinancialEntries().observe(getViewLifecycleOwner(), entries -> {
            if (entries != null) {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, entries);
                financialListView.setAdapter(adapter);

                // Set item click listener to show popup
                financialListView.setOnItemClickListener((parent, view, position, id) -> {
                    String selectedEntry = entries.get(position);
                    showPlayerFinancePopup(selectedEntry);


                });
            } else {
                showToast("No financial entries to display.");
            }
        });
    }

    private void showPlayerFinancePopup(String playerInfo) {
        // Inflate custom popup layout
        PlayerFinancePopupBinding popupBinding = PlayerFinancePopupBinding.inflate(LayoutInflater.from(mContext));

        // Pre-fill data if necessary (e.g., player name)
        popupBinding.popupTitle.setText(String.format("Finances Updater For (%s)", playerInfo));

        // Build the AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setView(popupBinding.getRoot())
                .setPositiveButton("Update", (dialog, which) -> {
                    String cashAmount = popupBinding.cashInput.getText().toString();
                    String eTransferAmount = popupBinding.eTransferInput.getText().toString();
//                    boolean didUpdate = interactor.handlePaymentStatusUpdate(cashAmount, eTransferAmount, playerInfo);
                    if (interactor.safeParseInt(cashAmount) > 0 || interactor.safeParseInt(eTransferAmount) > 0) {
                        String participantID = FinanceInteractor.extractUntilFirstComma(playerInfo);
                        int participantIDParsed = Integer.parseInt(participantID);
                        dataAccess.modifyParticipantPaymentStatus(participantIDParsed);

                        Map<Integer, Participant> currentPaymentState = dataAccess.getParticipantPaymentStatus();
                        List<String> formattedEntries = new FinanceOutputData().updateFinancialEntries(currentPaymentState);

                        // turn into list and then update the financial entries
                        financeViewModel.updateFinancialEntries(formattedEntries);

                        showToast(String.format("Player %s payment status was marked as paid.", playerInfo));
                    } else {
                        showToast("No data was updated.");
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void showToast(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }
}
