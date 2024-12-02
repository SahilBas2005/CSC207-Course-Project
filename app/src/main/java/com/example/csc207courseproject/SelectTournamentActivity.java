package com.example.csc207courseproject;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.csc207courseproject.interface_adapter.select_tournament.SelectTournamentController;
import com.example.csc207courseproject.interface_adapter.select_tournament.SelectTournamentViewModel;

/**
 * The View for the Select Tournament Use Case.
 */
public class SelectTournamentActivity extends AppCompatActivity implements PropertyChangeListener {

    private static SelectTournamentController selectTournamentController;
    private static SelectTournamentViewModel selectTournamentViewModel;

    /**
     * This is an initialization method for this activity. It is called only once, at the start of the application.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_tournament);

        selectTournamentViewModel.addPropertyChangeListener(this);
    }

    /**
     * This method is called when the select tournament screen becomes visible to the user.
     */
    @Override
    protected void onStart() {
        super.onStart();
        // get the list view
        final ListView tournamentViewList = findViewById(R.id.tournament_list);
        // get the no-tournament message
        final TextView noTournamentText = findViewById(R.id.no_tournament_message);

        // get the user's tournaments
        final List<String> tournamentNames = selectTournamentViewModel.getState().getTournamentNames();
        final List<Integer> tournamentIds = selectTournamentViewModel.getState().getTournamentIds();

        if (tournamentNames.isEmpty()) {
            tournamentViewList.setVisibility(View.GONE);
            noTournamentText.setVisibility(View.VISIBLE);
        }
        else {
            // pass the tournament names to the adapter, which will then show them within the list view
            final ArrayAdapter arrayAdapter =
                    new ArrayAdapter(this, android.R.layout.simple_list_item_1, tournamentNames);
            tournamentViewList.setAdapter(arrayAdapter);

            // add an on-click listener to each item
            tournamentViewList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                    // pos is the index of the element that was clicked
                    final Integer selectedTournamentId = tournamentIds.get(pos);
                    selectTournamentController.execute(selectedTournamentId);
                }
            });
        }
    }

    /**
     * Listens for a property change event fired by the SelectTournamentPresenter.
     * @param evt the property change event fired by the presenter
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case "tournamentsuccess":
                final Intent switchToEventView = new Intent(this, SelectEventActivity.class);
                startActivity(switchToEventView);
                break;
            case "tournamentfail":
                Toast.makeText(this, "Failed to select tournament. Please try again!",
                    Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * Sets the selectTournamentController.
     * @param selectTournamentController the controller to set to
     */
    public static void setSelectTournamentController(SelectTournamentController selectTournamentController) {
        SelectTournamentActivity.selectTournamentController = selectTournamentController;
    }

    /**
     * Sets the selectTournamentViewModel.
     * @param selectTournamentViewModel the view model to set to
     */
    public static void setSelectTournamentViewModel(SelectTournamentViewModel selectTournamentViewModel) {
        SelectTournamentActivity.selectTournamentViewModel = selectTournamentViewModel;
    }
}
