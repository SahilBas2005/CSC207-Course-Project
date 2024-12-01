package com.example.csc207courseproject.entities;

import java.util.*;

/**
 * An entity representing an event in a tournament.
 */
public class EventData {
    private static int tournamentId;
    private static int eventId;
    private static String eventName;
    private static Map<Integer, Entrant> entrants;
    private static Map<Integer, Station> stations;
    private static Map<Integer, Participant> participants;
    private static SortedMap<String, Integer> characterIds;
    private static SortedMap<String, Integer> phaseIds;
    private static final List<String> possibleTags = new ArrayList<>();

    public static void createEventData(int tourneyId, int eId, String eName, Map<Integer, Entrant> es,
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
        possibleTags.add("Stream setup");
        possibleTags.add("No DLC");
        possibleTags.add("No GCC adapter");
    }

    /**
     * Adds the parameter tag to possible tags.
     * @param tag The new tag
     */
    public static void addPossibleTag(String tag) {
        possibleTags.add(tag);
    }

    /**
     * Converts an entrant id to their name.
     * @param id Entrant id
     * @return Entrant name
     */
    public static String entrantIdToString(int id){
        return entrants.get(id).toString();
    }

    /**
     * Gets the entrant from the corresponding id.
     * @param id The entrant id
     * @return The entrant
     */
    public static Entrant getEntrant(int id) {
        return entrants.get(id);
    }

    /**
     * Gets the participant from the corresponding id.
     * @param id The participant id
     * @return The participant
     */
    public static Participant getParticipant(int id) {
        return participants.get(id);
    }

    /**
     * Converts a participant id to their name.
     * @param id Participant id
     * @return Participant name
     */
    public static String participantIdToString(int id){
        return participants.get(id).toString();
    }

    public static SortedMap<String, Integer> getPhaseIds() {
        return phaseIds;
    }

    public static int getEventId() {
        return eventId;
    }

    public static String getEventName() {
        return eventName;
    }

    public static Map<Integer, Entrant> getEntrants() {
        return entrants;
    }

    public static Map<Integer, Participant> getParticipants() {return participants;}

    public static int getTournamentId() {
        return tournamentId;
    }

    public static List<String> getPossibleTags() {
        return possibleTags;
    }

    public static Map<Integer, Station> getStations() {
        return stations;
    }  
      
    public static SortedMap<String, Integer> getCharacterIds() {
        return characterIds;
    }

    public static void setCharacterIds(SortedMap<String, Integer> characterIds) {
        EventData.characterIds = characterIds;
    }

    public static SortedMap<String, Integer> getCharacters() {
        return characterIds;
    }
}
