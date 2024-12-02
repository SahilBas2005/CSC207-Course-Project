package com.example.csc207courseproject.interface_adapter.report_set;

import com.example.csc207courseproject.entities.ReportSetData;

import java.util.ArrayList;
import java.util.List;


public class ReportSetState {

    private ReportSetData currentSet;
    private List<ReportSetData> ongoingSets;

    // Attributes accessible for display in the ReportSetFragment
    private int p1Wins = 0;
    private int p2Wins = 0;
    private List<Boolean> p1ButtonPresses = new ArrayList<>();
    private List<Boolean> p2ButtonPresses = new ArrayList<>();
    private boolean setOver = false;

    public ReportSetState() {
        p1ButtonPresses.add(false);
        p2ButtonPresses.add(false);
    }

    public boolean isSetOver() {
        return setOver;
    }

    public void setSetOver(boolean setOver) {
        this.setOver = setOver;
    }

    public List<Boolean> getP1ButtonPresses () {
        return p1ButtonPresses;
    }
    public List<Boolean> getP2ButtonPresses () {
        return p2ButtonPresses;
    }

    public void setP1ButtonPresses(List<Boolean> p1ButtonPresses) {
        this.p1ButtonPresses = p1ButtonPresses;
    }

    public void setP2ButtonPresses(List<Boolean> p2ButtonPresses) {
        this.p2ButtonPresses = p2ButtonPresses;
    }

    public int getP1Wins() {
        return p1Wins;
    }

    public int getP2Wins() {
        return p2Wins;
    }

    public void setP1Wins(int p1Wins) {
        this.p1Wins = p1Wins;
    }

    public void setP2Wins(int p2Wins) {
        this.p2Wins = p2Wins;
    }

    public void setReportedSetIDs(List<Integer> reportedSetIDs) {
        this.reportedSetIDs = reportedSetIDs;
    }

    // This Arraylist allows us to keep track of the recently reported sets locally
    // so that the local menus are updated in real time rather than after the API
    // calls update, which can take up to a minute
    private List<Integer> reportedSetIDs = new ArrayList<>();
    public void setCurrentSet(ReportSetData currentSet) {
        this.currentSet = currentSet;
    }

    public void setOngoingSets(List<ReportSetData> ongoingSets) {
        this.ongoingSets = ongoingSets;
    }

    public List<Integer> getReportedSetIDs() {return reportedSetIDs;}

    public List<ReportSetData> getOngoingSets() {return ongoingSets;}

    public ReportSetData getCurrentSet() {return currentSet;}



}
