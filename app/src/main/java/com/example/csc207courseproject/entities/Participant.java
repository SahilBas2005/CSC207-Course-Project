package com.example.csc207courseproject.entities;

import org.jetbrains.annotations.NotNull;

public class Participant {
    private int participantId;
    private int userId;
    private String name;
    private String sponsor;
    private boolean hasPaid = false;

    public Participant(int participantId, int userId, String name, String sponsor) {
        this.participantId = participantId;
        this.userId = userId;
        this.name = name;
        this.sponsor = sponsor;
    }

    public int getParticipantId() {
        return participantId;
    }

    public int getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getSponsor() {
        return sponsor;
    }

    public boolean isPaid() {
        return hasPaid;
    }

    public void markAsPaid() {
        hasPaid = true;
    }

    @NotNull
    @Override
    public String toString() {
        return sponsor + " " + name;
    }
}
