package com.example.csc207courseproject.ui.report;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.example.csc207courseproject.R;
import com.example.csc207courseproject.databinding.FragmentReportBinding;
import com.example.csc207courseproject.entities.SetData;
import com.example.csc207courseproject.interface_adapter.ongoing_sets.OngoingSetsController;
import com.example.csc207courseproject.interface_adapter.report_set.ReportSetState;
import com.example.csc207courseproject.interface_adapter.upcoming_sets.UpcomingSetsController;
import com.example.csc207courseproject.ui.AppFragment;
import com.example.csc207courseproject.ui.call.CallViewModel;
import org.jetbrains.annotations.NotNull;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class ReportFragment extends AppFragment implements PropertyChangeListener {

    private static ReportViewModel reportViewModel;
    private static OngoingSetsController ongoingSetsController;

    private FragmentReportBinding binding;
    private NavController navc;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        reportViewModel.addPropertyChangeListener(this);

        binding = FragmentReportBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ongoingSetsController.execute();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ReportSetFragment.setReportViewModel(reportViewModel);

        navc = Navigation.findNavController(view);
    }

    private void createDisplay() {
        ReportSetState currentState = reportViewModel.getState();
        List<String> setDisplay = new ArrayList<>();
        ListView setsView = binding.ongoingSets;
        List<SetData> sets = currentState.getOngoingSets();

        // If there are no current ongoing sets, then display that there are no ongoing sets
        // Otherwise, create the set display menu

        if(!sets.isEmpty()) {
            binding.noOngoingSets.setVisibility(View.INVISIBLE);
            for (SetData set : sets) {
                setDisplay.add(set.toString());
            }
        } else {
            binding.noOngoingSets.setVisibility(View.VISIBLE);
        }
        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, setDisplay);
        setsView.setAdapter(itemsAdapter);
        setsView.setOnItemClickListener((list, view, position, id) -> {
            reportViewModel.getState().setCurrentSet(sets.get(position));
            navc.navigate(R.id.action_nav_report_to_reportSetFragment);

        });
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case "getsetssuccess": createDisplay(); break;
            case "getsetsfail": Log.d("Fail", "fail"); break;
        }
    }

    public static void setOngoingSetsController(OngoingSetsController controller) {
        ongoingSetsController = controller;
    }

    public static void setReportViewModel(ReportViewModel viewModel) {reportViewModel = viewModel;}
}