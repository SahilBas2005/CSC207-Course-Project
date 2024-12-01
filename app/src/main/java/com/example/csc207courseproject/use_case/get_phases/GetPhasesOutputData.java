package com.example.csc207courseproject.use_case.get_phases;

import java.util.List;

public class GetPhasesOutputData {

    private final List<String> phases;


    public GetPhasesOutputData(List<String> phases) {
        this.phases = phases;
    }

    public List<String> getPhases() {
        return phases;
    }
}
