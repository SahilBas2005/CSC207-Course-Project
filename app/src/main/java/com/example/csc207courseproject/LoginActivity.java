package com.example.csc207courseproject;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.csc207courseproject.interface_adapter.login.LoginController;
import com.example.csc207courseproject.interface_adapter.login.LoginViewModel;

/**
 * The View for the Login Use Case and main app.
 */
public class LoginActivity extends AppCompatActivity implements PropertyChangeListener {

    private static LoginController loginController;
    private static LoginViewModel loginViewModel;

    /**
     * This is an initialization method for this activity. It is called only once, at the start of the application.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final MainBuilder mainBuilder = new MainBuilder();
        mainBuilder.addLoginView()
                .addTournamentView()
                .addEventView()
                .addSeedingView()
                .addCallView()
                .addReportView()
                .addFinanceView()
                .addLoginUseCase()
                .addSelectTournamentUseCase()
                .addGetPhasesUseCase()
                .addSelectEventUseCase()
                .addMutateSeedingUseCase()
                .addUpdateSeedingUseCase()
                .addSelectPhaseUseCase()
                .addUpcomingSetsUseCase()
                .addGetStationsUseCase()
                .addFindStationUseCase()
                .addAddStationUseCase()
                .addDeclineSetUseCase()
                .addCallSetUseCase()
                .addReportGameUseCase()
                .addReportSetUseCase()
                .addOngoingSetsUseCase()
                .addGetFinanceUseCase()
                .addModifyFinanceUseCase()
                .addExportFinanceUseCase();

        loginViewModel.addPropertyChangeListener(this);
    }

    /**
     * This method is called when the login screen becomes visible to the user.
     */
    @Override
    protected void onStart() {
        super.onStart();
        final Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String authUrl = loginController.execute();
                if (authUrl != null) {
                    // Create an Intent to open the URL in the browser
                    final Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(authUrl));
                    // Launch the browser
                    startActivity(browserIntent);
                }
            }
        });
    }

    /**
     * Listens for a property change event fired by the LoginPresenter.
     * @param evt the property change event fired by the presenter
     */
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case "loginsuccess":
                Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
                final Intent switchToTournamentView = new Intent(this, SelectTournamentActivity.class);
                startActivity(switchToTournamentView);
                break;
            case "loginfail":
                Toast.makeText(this, "Login failed. Please try again!", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * Sets the loginController.
     * @param loginController the controller to set to
     */
    public static void setLoginController(LoginController loginController) {
        LoginActivity.loginController = loginController;
    }

    /**
     * Sets the loginViewModel.
     * @param loginViewModel the view model to set to.
     */
    public static void setLoginViewModel(LoginViewModel loginViewModel) {
        LoginActivity.loginViewModel = loginViewModel;
    }
}
