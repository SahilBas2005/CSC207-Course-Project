package com.example.csc207courseproject.ui.finance;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.csc207courseproject.data_access.APIDataAccessObject;
import com.example.csc207courseproject.databinding.FragmentFinanceBinding;
import com.example.csc207courseproject.databinding.PlayerFinancePopupBinding;
import com.example.csc207courseproject.entities.Participant;
import com.example.csc207courseproject.use_case.finance.FinanceInputBoundary;
import com.example.csc207courseproject.use_case.finance.FinanceInteractor;
import com.example.csc207courseproject.use_case.finance.FinanceOutputBoundary;
import com.example.csc207courseproject.use_case.finance.FinanceOutputData;

import java.util.List;
import java.util.Map;

// PRESENTER
public class FinanceFragment extends Fragment {

    private FragmentFinanceBinding binding;
    private FinanceViewModel financeViewModel;
    private Context mContext;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Set up ViewModel
        financeViewModel = new ViewModelProvider(this).get(FinanceViewModel.class);
        // Set up binding
        binding = FragmentFinanceBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Observe and display the list
        observeFinancialEntries();

        binding.exportButton.setOnClickListener(v -> financeViewModel.exportParticipantPaymentData(requireContext()));

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
                    // part 1 modifiying the display: call the viewmodel, call the controller and return some modified list and then update through the viewmodel
                    financeViewModel.modifyParticipantPaymentStatus(cashAmount, eTransferAmount, playerInfo);
                    // part 2 display the toast, in the interactor have it call the show toast method
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


}
