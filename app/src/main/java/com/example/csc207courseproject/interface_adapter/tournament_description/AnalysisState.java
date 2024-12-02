package com.example.csc207courseproject.interface_adapter.tournament_description;

/**
 * A state containing the tournament description to be displayed to the user in the Analysis view.
 */
public class AnalysisState {
    private String aiMessage ;
    private String error = "";


    public void setError(String error) {

        this.error = error;
    }
    public void setaiMessage(String aiMessage) {

        this.aiMessage = aiMessage;
    }


    public String getError() {

        return error;
    }
    public String getaiMessage() {

        return aiMessage;
    }

    public void updateAiMessage(String aiMessage) {
        this.aiMessage = aiMessage;}
}