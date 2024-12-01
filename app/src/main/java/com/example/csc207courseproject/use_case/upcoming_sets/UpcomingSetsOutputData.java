package com.example.csc207courseproject.use_case.upcoming_sets;

import com.example.csc207courseproject.entities.CallSetData;

import java.util.List;

public class UpcomingSetsOutputData {

    private final List<CallSetData> upcomingSets;


    public UpcomingSetsOutputData(List<CallSetData> upcomingSets) {
        this.upcomingSets = upcomingSets;
    }

    public List<CallSetData> getUpcomingSets() {
        return upcomingSets;
    }
}
