package com.example.csc207courseproject.interface_adapter.call_set;

import com.example.csc207courseproject.entities.CallSetData;
import com.example.csc207courseproject.entities.Station;

import java.util.ArrayList;
import java.util.List;

public class CallSetState {
        private CallSetData currentSet;
        private List<Station> stations;
        private final List<Station> localStations = new ArrayList<>();
        private List<CallSetData> upcomingSets;
        private boolean eventStarted = false;
        private Station openStream;

        // This Arraylist allows us to keep track of the recently called sets locally
        // so that the local menus are updated in real time rather than after the API
        // calls update, which can take up to a minute
        private final List<Integer> calledSetIDs = new ArrayList<>();

        public CallSetData getCurrentSet() {return currentSet;}

        public void setCurrentSet(CallSetData currentSet) {this.currentSet = currentSet;}

        public List<Integer> getCalledSetIDs() {return calledSetIDs;}

        public void setEventStarted(boolean eventStarted) {this.eventStarted = eventStarted;}

        public boolean isEventStarted() {return eventStarted;}

        public void setUpcomingSets(List<CallSetData> upcomingSets) {
                this.upcomingSets = upcomingSets;
        }

        public List<CallSetData> getUpcomingSets() {
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

        public Station getOpenStream() {return openStream;}

        public void setOpenStream(Station openStream) {this.openStream = openStream;}
}
