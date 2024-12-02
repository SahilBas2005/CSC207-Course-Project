package com.example.csc207courseproject.use_case.ongoing_sets;

import com.example.csc207courseproject.entities.ReportSetData;

import java.util.List;

public class OngoingSetsOutputData {

    private final List<ReportSetData> ongoingSets;


    public OngoingSetsOutputData(List<ReportSetData> ongoingSets) {
        this.ongoingSets = ongoingSets;
    }

    public List<ReportSetData> getOngoingSets() {
        return ongoingSets;
    }
}
