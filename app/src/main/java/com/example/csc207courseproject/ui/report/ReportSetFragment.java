package com.example.csc207courseproject.ui.report;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.example.csc207courseproject.R;
import com.example.csc207courseproject.databinding.FragmentReportSetBinding;
import com.example.csc207courseproject.entities.EventData;
import com.example.csc207courseproject.entities.Game;
import com.example.csc207courseproject.interface_adapter.report_game.ReportGameController;
import com.example.csc207courseproject.interface_adapter.report_set.ReportSetController;
import com.example.csc207courseproject.interface_adapter.report_set.ReportSetState;
import com.example.csc207courseproject.ui.AppFragment;
import org.jetbrains.annotations.NotNull;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ReportSetFragment extends AppFragment implements PropertyChangeListener {

    private static ReportViewModel reportViewModel;

    private static ReportSetController reportSetController;

    private static ReportGameController reportGameController;

    private NavController navController;

    private FragmentReportSetBinding binding;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        reportViewModel.addPropertyChangeListener(this);
        binding = FragmentReportSetBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ReportSetState currentState = reportViewModel.getState();

        TextView text = binding.playersTitle;
        text.setText(currentState.getCurrentSet().toString());

        updateScore();
        createMutateButton();
        createGamesDisplay();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }

    public void createGamesDisplay(){
        ReportSetState currentState = reportViewModel.getState();
        ListView gamesView = binding.gamesList;
        List<Game> games = currentState.getCurrentSet().getGames();
        
        ArrayAdapter<Game> gameAdapter =
                new ArrayAdapter<Game>(mContext, android.R.layout.simple_list_item_1, games) {
                    @Override
                    public int getCount() {
                        return games.size();
                    }

                    @Override
                    public Game getItem(int position) {
                        return games.get(position);
                    }

                    @Override
                    public long getItemId(int position) {
                        return position;
                    }

                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {

                        convertView = getLayoutInflater().inflate(R.layout.list_games, parent, false);

                        // Create title
                        TextView gameTitle = convertView.findViewById(R.id.game_num_title);
                        String gameText = "Game #" + (position + 1) + ":";
                        gameTitle.setText(gameText);

                        // Create p1/p2 win buttons and make them exclusive
                        ToggleButton p1WinButton = convertView.findViewById(R.id.player1_win);
                        boolean p1IsClicked = currentState.getP1ButtonPresses().get(position);
                        Log.d("p1isclicked", String.valueOf(p1IsClicked));
                        p1WinButton.setChecked(p1IsClicked);
                        p1WinButton.setEnabled(!p1IsClicked);

                        ToggleButton p2WinButton = convertView.findViewById(R.id.player2_win);
                        boolean p2IsClicked = currentState.getP2ButtonPresses().get(position);
                        Log.d("p2isclicked", String.valueOf(p2IsClicked));
                        p2WinButton.setChecked(p2IsClicked);
                        p2WinButton.setEnabled(!p2IsClicked);

                        p1WinButton.setOnClickListener(view -> {
                            p2WinButton.setChecked(false);
                            p2WinButton.setEnabled(true);

                            p1WinButton.setEnabled(false);

                            reportGameController.execute(position + 1, 1, "", "");
                            updateScore();
                        });
                        p2WinButton.setOnClickListener(view ->{
                            p1WinButton.setChecked(false);
                            p1WinButton.setEnabled(true);

                            p2WinButton.setEnabled(false);

                            reportGameController.execute(position + 1, 2, "", "");
                            updateScore();
                        });

                        // Create p1 character list
                        Spinner p1CharSelect = convertView.findViewById(R.id.p1_char_select);
                        Set<String> possibleChars = EventData.getCharacters().keySet();
                        ArrayAdapter<String> chars1Adapter = new ArrayAdapter<>(mContext,
                                android.R.layout.simple_spinner_dropdown_item, new ArrayList<String>(possibleChars));
                        chars1Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        p1CharSelect.setAdapter(chars1Adapter);

                        // Create p2 character list
                        Spinner p2CharSelect = convertView.findViewById(R.id.p2_char_select);
                        ArrayAdapter<String> chars2Adapter = new ArrayAdapter<>(mContext,
                                android.R.layout.simple_spinner_dropdown_item, new ArrayList<String>(possibleChars));
                        chars1Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        p2CharSelect.setAdapter(chars2Adapter);


                        return convertView;
                    }
                };

        gamesView.setAdapter(gameAdapter);
    }

    private void updateScore(){

        ReportSetState currentState = reportViewModel.getState();

        TextView p1Score = binding.player1Score;
        p1Score.setText(String.valueOf(currentState.getP1Wins()));

        TextView p2Score = binding.player2Score;
        p2Score.setText(String.valueOf(currentState.getP2Wins()));

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        reportViewModel.removePropertyChangeListener(this);
        binding = null;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case "reportsetsuccess":
                showToast("The set has been reported to Start.gg!");
                navController.navigateUp();
                break;
            case "reportgamesuccess": break;
            case "apicallerror": showToast("We can't reach Start.gg right now."); break;
            case "incompletesetinfo": showToast("The set information is not complete!"); break;
        }
    }

    public static void setReportSetController(ReportSetController controller) {
        reportSetController = controller;
    }

    public static void setReportGameController(ReportGameController controller) {
        reportGameController = controller;
    }

    public static void setReportViewModel(ReportViewModel viewModel) {
        reportViewModel = viewModel;
    }

    //Function for the report to start.gg option
    private void createMutateButton(){
        Button mutateSetButton = binding.mutateSetButton;
        mutateSetButton.setOnClickListener(view -> reportSetController.execute(
                binding.isP1DQ.isChecked(), binding.isP2DQ.isChecked()));
    }

}
