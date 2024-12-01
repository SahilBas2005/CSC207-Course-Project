package com.example.csc207courseproject.use_case.report_game;

public class ReportGameOutputData {

    private int p2Score;
    private int p1Score;
    private boolean setOver;
    private String p1Char;
    private String p2Char;


    public ReportGameOutputData(int p1Score, int p2Score, boolean setOver, String p1Char, String p2Char) {
        this.p1Score = p1Score;
        this.p2Score = p2Score;
        this.setOver = setOver;
        this.p1Char = p1Char;
        this.p2Char = p2Char;
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
