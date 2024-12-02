package com.example.csc207courseproject.ui.report;

import com.example.csc207courseproject.interface_adapter.ViewModel;
import com.example.csc207courseproject.interface_adapter.report_set.ReportSetState;

/**
 * The ViewModel for the Report View.
 */
public class ReportViewModel extends ViewModel<ReportSetState> {

    public ReportViewModel() {
        super("report");
        setState(new ReportSetState());
    }
}
