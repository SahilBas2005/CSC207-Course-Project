package com.example.csc207courseproject.interface_adapter.ongoing_sets;

import android.util.Log;
import android.view.View;
import com.example.csc207courseproject.entities.SetData;
import com.example.csc207courseproject.interface_adapter.ViewManagerModel;
import com.example.csc207courseproject.interface_adapter.call_set.CallSetState;
import com.example.csc207courseproject.interface_adapter.report_set.ReportSetState;
import com.example.csc207courseproject.ui.report.ReportViewModel;
import com.example.csc207courseproject.use_case.ongoing_sets.OngoingSetsOutputBoundary;
import com.example.csc207courseproject.use_case.ongoing_sets.OngoingSetsOutputData;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class OngoingSetsPresenter implements OngoingSetsOutputBoundary {

    private final ReportViewModel viewModel;
    private final ViewManagerModel viewManagerModel;

    public OngoingSetsPresenter(ReportViewModel viewModel, ViewManagerModel viewManagerModel) {
        this.viewModel = viewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void prepareSuccessView(OngoingSetsOutputData outputData) {
        final ReportSetState currentState = viewModel.getState();

        List<SetData> sets = outputData.getOngoingSets();
        List<SetData> repeats = new ArrayList<>();

        // Remove the recently reported sets from the state's ongoing sets variable

        for (SetData set : sets) {
            if (currentState.getReportedSetIDs().contains(set.getSetID())) {
                repeats.add(set);
            }
        }

        sets.removeAll(repeats);

        currentState.setOngoingSets(sets);
        viewModel.firePropertyChanged("getsetssuccess");
    }

    @Override
    public void prepareFailView(String errorMessage) {
        viewModel.firePropertyChanged("getsetsfail");
    }

}
