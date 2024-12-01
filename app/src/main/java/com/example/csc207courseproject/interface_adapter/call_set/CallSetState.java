package com.example.csc207courseproject.interface_adapter.call_set;

import com.example.csc207courseproject.entities.SetData;
import com.example.csc207courseproject.entities.Station;

import java.util.ArrayList;
import java.util.List;

public class CallSetState {
        private SetData currentSet;
        private List<Station> stations;
        private final List<Station> localStations = new ArrayList<>();
        private List<SetData> upcomingSets;

        // This Arraylist allows us to keep track of the recently called sets locally
        // so that the local menus are updated in real time rather than after the API
        // calls update, which can take up to a minute
        private List<Integer> calledSetIDs = new ArrayList<>();

        public SetData getCurrentSet() {return currentSet;}

        public void setCurrentSet(SetData currentSet) {this.currentSet = currentSet;}

        public List<Integer> getCalledSetIDs() {return calledSetIDs;}

        public void addCalledSetID(int newID) {
                if (calledSetIDs.size() == 10) {
                        calledSetIDs.remove(0);
                }
                calledSetIDs.add(newID);
        }

        public void setUpcomingSets(List<SetData> upcomingSets) {
                this.upcomingSets = upcomingSets;
        }

        public List<SetData> getUpcomingSets() {
                return upcomingSets;
        }

        public List<Station> getStations() {
                return stations;
        }

        public void setStations(List<Station> stations) {
                this.stations = stations;
        }

        public List<Station> getLocalStations() {
                return localStations;
        }

        public void addStation(Station station) {
                localStations.add(station);
                stations.add(station);
        }

        /**
         * Checks if there is an open stream setup.
         * @return True if there is an open stream
         */
        public boolean isStreamOpen() {
                for(Station station : stations) {
                        if (station.isStream() && station.isNotOccupied()){
                                return true;
                        }
                }
                return false;
        }

        /**
         * Finds and assigns an open stream station to the current set.
         */
        public void findStream(){
                for (Station station : stations) {
                        if (station.isNotOccupied() && station.isStream()) {
                                currentSet.setStation(station);
                        }
                }
        }

        /**
         * Updates the state to decline the current set.
         * @param tag The reason for the decline
         * @param p1Applies True if the tag applies to player 1
         * @param p2Applies True if the tag applies to player 2
         */
        public void declineSet(String tag, boolean p1Applies, boolean p2Applies){
                if (p1Applies && !currentSet.getPlayers()[0].getTags().contains(tag)) {
                        currentSet.getPlayers()[0].getTags().add(tag);
                }
                if (p2Applies  && !currentSet.getPlayers()[1].getTags().contains(tag)){
                        currentSet.getPlayers()[1].getTags().add(tag);
                }
                if (!currentSet.getStation().hasTag(tag)) {
                        currentSet.getStation().addTag(tag);
                }
        }


}
