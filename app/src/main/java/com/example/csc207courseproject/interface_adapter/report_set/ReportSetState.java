package com.example.csc207courseproject.interface_adapter.report_set;

import com.example.csc207courseproject.entities.SetData;


public class ReportSetState {

    private SetData currentSet;
    private boolean setOver;

    public ReportSetState(SetData currentSet) {
        this.currentSet = currentSet;
        this.setOver = false;
    }
    public SetData getSetData() {return currentSet;}
    public boolean getSetOver() {return setOver;}

    // Reports the game into the game object, updates the player count scores, and checks if the set is done
    public void reportGame(int gameNumber, int winnerID, String p1Char, String p2Char, boolean p1Won) {
        currentSet.getGame(gameNumber - 1).report(winnerID, p1Char, p2Char);

        if (p1Won){
            currentSet.addP1Win();
        } else {
            currentSet.addP2Win();
        }

        setOver = currentSet.isSetOver();

        if (!setOver){
            currentSet.addGame();
        }

    }


}