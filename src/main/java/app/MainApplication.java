package app;

import data_access.APIDataAccessObject;

public class MainApplication {
    public static void main(String[] args) {
        APIDataAccessObject dao = new APIDataAccessObject();
        dao.getEvent();
//        dao.getPhaseSeeding();
    }
}
