package com.example.csc207courseproject.entities;

/**
 * An entity representing an entrant in an event
 */
public class Entrant {
    private final String[] names;
    private final String[] sponsors;
    private final int id;
    private final int[] userIDs;
    private boolean paid;

    /**
     * Entrant constructor.
     * @param names Entrant names
     * @param sponsors Entrant sponsors
     * @param id Entrant ID
     */
    public Entrant(String[] names, String[] sponsors, int id, int[] userIDs, boolean paid) {
        this.names = names;
        this.sponsors = sponsors;
        this.id = id;
        this.userIDs = userIDs;
        this.paid = paid;
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

    public boolean getPaidStatus() {
        return this.paid;
    }

    @Override
    public String toString() {
        return EventData.idToString(id);
    }
}
