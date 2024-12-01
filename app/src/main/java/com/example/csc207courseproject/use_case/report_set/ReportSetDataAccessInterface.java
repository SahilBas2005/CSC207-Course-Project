package com.example.csc207courseproject.use_case.report_set;

import com.example.csc207courseproject.entities.Game;

import java.util.List;
import java.util.Map;

public interface ReportSetDataAccessInterface {

    /**
     * Reports the results for a certain match to start.gg
     * ADD PARAM TAGS WHEN IMPLEMENTED
     */
    void reportSet(int setID, int winnerId, List<Game> games, boolean hasDQ);
}