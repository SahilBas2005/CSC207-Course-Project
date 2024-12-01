package com.example.csc207courseproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.csc207courseproject.interface_adapter.login.LoginController;
import com.example.csc207courseproject.interface_adapter.login.LoginViewModel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class LoginActivity extends AppCompatActivity implements PropertyChangeListener {

    private static LoginController loginController;
    private static LoginViewModel loginViewModel;

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
                .addLoginUseCase()
                .addSelectTournamentUseCase()
                .addSelectEventUseCase()
                .addMutateSeedingUseCase()
                .addUpdateSeedingUseCase()
                .addSelectPhaseUseCase();

        loginViewModel.addPropertyChangeListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String authURL = loginController.execute();
                if (authURL != null) {
                    // Create an Intent to open the URL in the browser
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(authURL));
                    startActivity(browserIntent);              // Launch the browser
                }
            }
        });
    }

    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case "loginsuccess":
                Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
                Intent switchToTournamentView = new Intent(this, SelectTournamentActivity.class);
                startActivity(switchToTournamentView);
                break;
            case "loginfail": Toast.makeText(this, "Login failed. Please try again!",
                    Toast.LENGTH_SHORT).show(); break;
        }
    }


    public static void setLoginController(LoginController loginController) {
        LoginActivity.loginController = loginController;
    }

    public static void setLoginViewModel(LoginViewModel loginViewModel) {
        LoginActivity.loginViewModel = loginViewModel;
    }
}
