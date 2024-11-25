package com.example.csc207courseproject.ui.finance;

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
import androidx.lifecycle.ViewModelProvider;

import com.example.csc207courseproject.databinding.FragmentFinanceBinding;

import java.util.List;

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

        return root;
    }

    private void observeFinancialEntries() {
        ListView financialListView = binding.seedsList;

        // Observe LiveData from the ViewModel
        financeViewModel.getFinancialEntries().observe(getViewLifecycleOwner(), entries -> {
            if (entries != null) {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, entries);
                financialListView.setAdapter(adapter);
            } else {
                showToast("No financial entries to display.");
            }
        });
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
