package use_case.report_set;

import java.util.List;
import java.util.Map;

public interface ReportSetDataAccessInterface {

    /**
            * Reports the results for a certain match to start.gg
     * ADD PARAM TAGS WHEN IMPLEMENTED
     */
    void reportSet(int setID, int winnerID, List<Map<String, Integer>> gameData);
}
