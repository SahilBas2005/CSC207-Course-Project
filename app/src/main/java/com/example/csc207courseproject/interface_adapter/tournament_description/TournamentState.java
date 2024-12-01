package com.example.csc207courseproject.interface_adapter.tournament_description;

import java.util.List;
import com.example.csc207courseproject.entities.EventData;

/**
 * A state containing the tournaments to be displayed to the user in the Select Tournament view.
 */
public class TournamentState {
    private static TournamentState instance;
    private Integer tournamentId;
    private String aiMessage = "Hello tournamentState";
    private String error = "";

    public static TournamentState getInstance() {
        if (instance == null) {
            instance = new TournamentState();
        }
        return instance;
    }

    public String eventName() {
        return EventData.getEventName();
    }

    public void setError(String error) {
        this.error = error;
    }
    public String getError() {
        return error;
    }

    public void setaiMessage(String aiMessage) {
        this.aiMessage = aiMessage;
    }
    public void updateaiMessage(String newMessage) {
        this.aiMessage = newMessage;
    }
    public String getaiMessage() {
        return aiMessage;
    }
}