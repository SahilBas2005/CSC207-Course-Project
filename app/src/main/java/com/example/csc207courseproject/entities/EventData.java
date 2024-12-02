package com.example.csc207courseproject.entities;

import java.util.*;

/**
 * An entity representing an event in a tournament.
 */
public class EventData {
    private final int tournamentId;
    private final int eventId;
    private final String eventName;
    private final Map<Integer, Entrant> entrants;
    private final Map<Integer, Station> stations;
    private final Map<Integer, Participant> participants;
    private SortedMap<String, Integer> characterIds;
    private final SortedMap<String, Integer> phaseIds;
    private final List<String> possibleTags ;

    // Ensure only one eventData is created for Singleton design pattern
    private static volatile EventData instance;

    private EventData(int tourneyId, int eId, String eName, Map<Integer, Entrant> es,
                      Map<Integer, Participant> ps, SortedMap<String, Integer> chars,
                      SortedMap<String, Integer> phases) {
        tournamentId = tourneyId;
        eventId = eId;
        eventName = eName;
        entrants = es;
        participants = ps;
        characterIds = chars;
        phaseIds = phases;
        stations = new HashMap<>();

        // Set initial tags
        possibleTags = new ArrayList<>();
        possibleTags.add("Stream setup");
        possibleTags.add("No DLC");
        possibleTags.add("No GCC adapter");

    }

    public static EventData createEventData(int tourneyId, int eId, String eName, Map<Integer, Entrant> es,
                                       Map<Integer, Participant> ps, SortedMap<String, Integer> chars,
                                       SortedMap<String, Integer> phases) {

        // Pass existing instance if it exists
        EventData resultInstance = instance;
        if (resultInstance != null) {
            return resultInstance;
        }

        // Ensure multiple eventDatas are not made over every thread
        synchronized (EventData.class) {
            if (instance == null) {
                instance = new EventData(tourneyId, eId, eName, es, ps, chars, phases);
            }
            return instance;
        }

    }

    public static EventData getEventData(){
        return instance;
    }

    /**
     * Adds the parameter tag to possible tags.
     * @param tag The new tag
     */
    public void addPossibleTag(String tag) {
        possibleTags.add(tag);
    }

    /**
     * Converts an entrant id to their name.
     * @param id Entrant id
     * @return Entrant name
     */
    public String entrantIdToString(int id){
        return entrants.get(id).toString();
    }

    /**
     * Gets the entrant from the corresponding id.
     * @param id The entrant id
     * @return The entrant
     */
    public Entrant getEntrant(int id) {
        return entrants.get(id);
    }

    /**
     * Gets the participant from the corresponding id.
     * @param id The participant id
     * @return The participant
     */
    public Participant getParticipant(int id) {
        return participants.get(id);
    }

    /**
     * Converts a participant id to their name.
     * @param id Participant id
     * @return Participant name
     */
    public String participantIdToString(int id){
        return participants.get(id).toString();
    }

    public SortedMap<String, Integer> getPhaseIds() {
        return phaseIds;
    }

    public int getEventId() {
        return eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public Map<Integer, Entrant> getEntrants() {
        return entrants;
    }

    public Map<Integer, Participant> getParticipants() {return participants;}

    public int getTournamentId() {
        return tournamentId;
    }

    public List<String> getPossibleTags() {
        return possibleTags;
    }

    public Map<Integer, Station> getStations() {
        return stations;
    }


    public SortedMap<String, Integer> getCharacterIds() {
        return characterIds;
    }
}
