package com.example.csc207courseproject.entities;


import org.jetbrains.annotations.NotNull;

public class CallSetData extends SetData {

    private Station station;


    public CallSetData(int setID, Entrant[] players) {
        super(setID, players);
    }

    public Station getStation() {return station;}

    public void setStation(Station station) {this.station = station;}

    /**
     * Checks if a set conflicts with the parameter station.
     * @param station The station being checked.
     * @return True if the set conflicts
     */
    public boolean conflictsWithStation(Station station){
        for (Entrant entrant : super.getPlayers()){
            for(String playerTag : entrant.getTags()){
                if (station.hasTag(playerTag)){
                    return true;
                }
            }
        }
        return false;
    }

    @NotNull
    @Override
    public String toString() {
        if (station == null){
            return super.toString();
        }
        // If there is a station, display it
        return super.toString() + " " + station.toString();
    }

}