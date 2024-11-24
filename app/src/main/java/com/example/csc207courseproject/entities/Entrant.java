package com.example.csc207courseproject.entities;

/**
 * An entity representing an entrant in an event
 */
public class Entrant {
    private final String[] names;
    private final String[] sponsors;
    private final int id;
    private final int[] userIDs;

    /**
     * Entrant constructor.
     * @param names Entrant names
     * @param sponsors Entrant sponsors
     * @param id Entrant ID
     */
    public Entrant(String[] names, String[] sponsors, int id, int[] userIDs) {
        this.names = names;
        this.sponsors = sponsors;
        this.id = id;
        this.userIDs = userIDs;
    }

    public String[] getNames() {
        return names;
    }

    public String[] getSponsors() {
        return sponsors;
    }

    public int getId() {
        return id;
    }

    public int[] getUserIDs() {
        return userIDs;
    }

    @Override
    public String toString() {
        return EventData.idToString(id);
    }
}
