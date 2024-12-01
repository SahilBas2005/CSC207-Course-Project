package com.example.csc207courseproject;

import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.csc207courseproject.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final MainBuilder mainBuilder = new MainBuilder();
        // Build app
        mainBuilder.createEventData()
                .addSeedingView()
                .addAnalysisView()
                .addMutateSeedingUseCase()
                .addUpdateSeedingUseCase()
                .addSelectPhaseUseCase()
                .addTournamentDescriptionUseCase()
                .addCallView();


        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_seeding,
                R.id.nav_call, R.id.nav_report, R.id.nav_finance, R.id.nav_analysis)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }



}