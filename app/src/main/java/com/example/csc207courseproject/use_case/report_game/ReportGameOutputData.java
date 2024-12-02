package com.example.csc207courseproject.use_case.report_game;

public class ReportGameOutputData {

    private int p2Score;
    private int p1Score;
    private boolean setOver;


    public ReportGameOutputData(int p1Score, int p2Score, boolean setOver) {
        this.p1Score = p1Score;
        this.p2Score = p2Score;
        this.setOver = setOver;
    }

    public int getP1Score() {
        return p1Score;
    }

    public int getP2Score() {
        return p2Score;
    }

    public boolean getSetOver() {
        return setOver;
    }


}
