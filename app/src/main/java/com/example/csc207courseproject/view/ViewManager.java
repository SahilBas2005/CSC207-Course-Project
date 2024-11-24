package com.example.csc207courseproject.view;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.example.csc207courseproject.interface_adapter.ViewManagerModel;


/**
 * The View Manager for the program. It listens for property change events
 * in the ViewManagerModel and updates which View should be visible.
 */

public class ViewManager implements PropertyChangeListener {
    private final ViewManagerModel viewManagerModel;

    public ViewManager(ViewManagerModel viewManagerModel) {
        this.viewManagerModel = viewManagerModel;
        this.viewManagerModel.addPropertyChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("state")) {
            final String viewModelName = (String) evt.getNewValue();
        }
    }

}
