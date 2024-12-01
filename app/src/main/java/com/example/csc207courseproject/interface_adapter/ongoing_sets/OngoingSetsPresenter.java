package com.example.csc207courseproject.interface_adapter.ongoing_sets;

import com.example.csc207courseproject.entities.SetData;
import com.example.csc207courseproject.interface_adapter.report_set.ReportSetState;
import com.example.csc207courseproject.ui.report.ReportViewModel;
import com.example.csc207courseproject.use_case.ongoing_sets.OngoingSetsOutputBoundary;
import com.example.csc207courseproject.use_case.ongoing_sets.OngoingSetsOutputData;

import java.util.ArrayList;
import java.util.List;

public class OngoingSetsPresenter implements OngoingSetsOutputBoundary {

    private final ReportViewModel viewModel;

    public OngoingSetsPresenter(ReportViewModel viewModel) {
        this.viewModel = viewModel;
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
    public void prepareFailView() {
        viewModel.firePropertyChanged("getsetsfail");
    }

}
