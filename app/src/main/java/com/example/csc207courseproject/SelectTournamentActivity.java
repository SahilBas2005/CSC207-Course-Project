package com.example.csc207courseproject;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.*;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.csc207courseproject.interface_adapter.select_tournament.SelectTournamentController;
import com.example.csc207courseproject.interface_adapter.select_tournament.SelectTournamentViewModel;
import com.example.csc207courseproject.interface_adapter.select_tournament.TournamentState;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SelectTournamentActivity extends AppCompatActivity implements PropertyChangeListener {

    private static SelectTournamentController selectTournamentController;
    private static SelectTournamentViewModel selectTournamentViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_tournament);

        selectTournamentViewModel.addPropertyChangeListener(this);
    }

    protected void onStart() {
        super.onStart();
        ListView tournamentViewList = findViewById(R.id.tournament_list);       // get the list view
        TextView noTournamentText = findViewById(R.id.no_tournament_message);

        // get the user's tournaments
        List<String> tournamentNames = selectTournamentViewModel.getState().getTournamentNames();
        List<Integer> tournamentIds = selectTournamentViewModel.getState().getTournamentIds();

        if (tournamentNames.isEmpty()) {
            tournamentViewList.setVisibility(View.GONE);
            noTournamentText.setVisibility(View.VISIBLE);
        }
        else {
            // pass the tournament names to the adapter, which will then show them within the list view
            ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, tournamentNames);
            tournamentViewList.setAdapter(arrayAdapter);

            // add an on-click listener to each item
            tournamentViewList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                    // pos is the index of the element that was clicked
                    Integer selectedTournamentId = tournamentIds.get(pos);
                    selectTournamentController.execute(selectedTournamentId);
                }
            });
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case "tournamentsuccess":
                Intent switchToEventView = new Intent(this, SelectEventActivity.class);
                startActivity(switchToEventView);
                break;
            case "tournamentfail":
                Toast.makeText(this, "Failed to select tournament. Please try again!",
                    Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public static void setSelectTournamentController(SelectTournamentController selectTournamentController) {
        SelectTournamentActivity.selectTournamentController = selectTournamentController;
    }

    public static void setSelectTournamentViewModel(SelectTournamentViewModel selectTournamentViewModel) {
        SelectTournamentActivity.selectTournamentViewModel = selectTournamentViewModel;
    }
}
