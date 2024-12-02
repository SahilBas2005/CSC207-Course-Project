
package com.example.csc207courseproject.interface_adapter.report_set;

import com.example.csc207courseproject.ui.report.ReportViewModel;
import com.example.csc207courseproject.use_case.report_set.ReportSetOutputBoundary;

import java.util.List;

public class ReportSetPresenter implements ReportSetOutputBoundary {

    private final ReportViewModel reportViewModel;

    public ReportSetPresenter(ReportViewModel reportViewModel) {
        this.reportViewModel = reportViewModel;
    }

    @Override
    public void prepareSuccessView() {
        int reportedID = reportViewModel.getState().getCurrentSet().getSetID();
        List<Integer> reportedSetIDs = reportViewModel.getState().getReportedSetIDs();
        // Only store 10 at any given time
        if (reportedSetIDs.size() == 10) {
            reportedSetIDs.remove(0);
        }
        reportedSetIDs.add(reportedID);
        reportViewModel.firePropertyChanged("reportsetsuccess");

    }

    @Override
    public void prepareFailView(String errorMessage) {
        reportViewModel.firePropertyChanged(errorMessage);
    }
}
