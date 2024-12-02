package com.example.csc207courseproject;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.csc207courseproject.interface_adapter.select_event.SelectEventController;
import com.example.csc207courseproject.interface_adapter.select_event.SelectEventViewModel;

/**
 * The View for the Select Event Use Case.
 */
public class SelectEventActivity extends AppCompatActivity implements PropertyChangeListener {

    private static SelectEventController selectEventController;
    private static SelectEventViewModel selectEventViewModel;

    /**
     * This is an initialization method for this activity. It is called only once, at the start of the application.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_event);

        selectEventViewModel.addPropertyChangeListener(this);
    }

    /**
     * This method is called when the select event screen becomes visible to the user.
     */
    @Override
    protected void onStart() {
        super.onStart();
        final ListView eventViewList = findViewById(R.id.event_list);
        final TextView noEventText = findViewById(R.id.no_events_message);

        final Integer tournamentId = selectEventViewModel.getState().getTournamentId();
        final List<String> eventNames = selectEventViewModel.getState().getEventNames();
        final List<Integer> eventIds = selectEventViewModel.getState().getEventIds();

        if (eventNames.isEmpty()) {
            eventViewList.setVisibility(View.GONE);
            noEventText.setVisibility(View.VISIBLE);
        }
        else {
            // pass the event names to the adapter, which will then show them within the list view
            final ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, eventNames);
            eventViewList.setAdapter(arrayAdapter);

            // add an on-click listener to each item
            eventViewList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                    // pos is the index of the element that was clicked
                    final String selectedEventName = eventNames.get(pos);
                    final Integer selectedEventId = eventIds.get(pos);
                    selectEventController.execute(tournamentId, selectedEventName, selectedEventId);
                }
            });
        }
    }

    /**
     * Listens for a property change event fired by the SelectEventPresenter.
     * @param evt the property change event fired by the presenter
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case "eventsuccess":
                final Intent switchToMainView = new Intent(this, MainActivity.class);
                startActivity(switchToMainView);
                break;
            case "eventfail":
                Toast.makeText(this, "Failed to select event. Please try again!",
                        Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * Sets the selectEventController.
     * @param selectEventController the controller to set to
     */
    public static void setSelectEventController(SelectEventController selectEventController) {
        SelectEventActivity.selectEventController = selectEventController;
    }

    /**
     * Sets the selectEventViewModel.
     * @param selectEventViewModel the view model to set to
     */
    public static void setSelectEventViewModel(SelectEventViewModel selectEventViewModel) {
        SelectEventActivity.selectEventViewModel = selectEventViewModel;
    }
}
