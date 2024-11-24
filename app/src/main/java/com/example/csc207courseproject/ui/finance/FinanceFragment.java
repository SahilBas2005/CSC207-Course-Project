package com.example.csc207courseproject.ui.finance;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.csc207courseproject.databinding.FragmentFinanceBinding;

public class FinanceFragment extends Fragment {

private FragmentFinanceBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        FinanceViewModel FInanceViewModel =
                new ViewModelProvider(this).get(FinanceViewModel.class);

    binding = FragmentFinanceBinding.inflate(inflater, container, false);
    View root = binding.getRoot();

        final TextView textView = binding.textFinance;
        FInanceViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

@Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}