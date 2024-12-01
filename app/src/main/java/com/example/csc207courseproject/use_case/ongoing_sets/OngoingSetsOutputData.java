package com.example.csc207courseproject.use_case.ongoing_sets;

import com.example.csc207courseproject.entities.SetData;

import java.util.List;

public class OngoingSetsOutputData {

    private final List<SetData> ongoingSets;


    public OngoingSetsOutputData(List<SetData> ongoingSets) {
        this.ongoingSets = ongoingSets;
    }

    public List<SetData> getOngoingSets() {
        return ongoingSets;
    }
}
