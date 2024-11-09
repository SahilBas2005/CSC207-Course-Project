package app;

import data_access.APIDataAccessObject;
import org.json.JSONArray;

import java.util.List;
import java.util.Objects;
import java.util.HashMap;

public class MainApplication {
    public static void main(String[] args) {
        APIDataAccessObject dao = new APIDataAccessObject();
//        HashMap<Integer, String> event = dao.getEvent();
//        System.out.println(event.keySet());
        HashMap<String, JSONArray> phasesMap = dao.getPhases();
        System.out.println(phasesMap.keySet());
        System.out.println(phasesMap.get("Top 8"));
    }
}
