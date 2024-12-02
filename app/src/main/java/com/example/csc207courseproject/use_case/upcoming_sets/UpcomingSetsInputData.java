package com.example.csc207courseproject.use_case.upcoming_sets;

import java.util.List;

/**
 * The Input Data for the find station Use Case.
 */
public class UpcomingSetsInputData {

    private final List<Integer> calledSetIds;

    public UpcomingSetsInputData(List<Integer> calledSetIds) {
        this.calledSetIds = calledSetIds;
    }

    public List<Integer> getcalledSetIds() {return calledSetIds;}

}
