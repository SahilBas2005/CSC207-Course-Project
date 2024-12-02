package com.example.csc207courseproject.ui.report;

import android.os.Bundle;
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

/**
 * The ReportSet UI, contains options for reporting certain games in a set, and mutating to Start.gg.
 */
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
        final View root = binding.getRoot();
        final ReportSetState currentState = reportViewModel.getState();

        final TextView text = binding.playersTitle;
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

    /**
     * Creates the list display of games and the buttons/dropdowns for game reporting
     */
    public void createGamesDisplay() {
        final ReportSetState currentState = reportViewModel.getState();
        final ListView gamesView = binding.gamesList;
        final List<Game> games = currentState.getCurrentSet().getGames();
        
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
                        final TextView gameTitle = convertView.findViewById(R.id.game_num_title);
                        final String gameText = "Game #" + (position + 1) + ":";
                        gameTitle.setText(gameText);

                        // Create p1/p2 win buttons and make them exclusive
                        final ToggleButton p1WinButton = convertView.findViewById(R.id.player1_win);
                        final boolean p1IsClicked = currentState.getP1ButtonPresses().get(position);
                        p1WinButton.setChecked(p1IsClicked);
                        p1WinButton.setEnabled(!p1IsClicked);

                        final ToggleButton p2WinButton = convertView.findViewById(R.id.player2_win);
                        final boolean p2IsClicked = currentState.getP2ButtonPresses().get(position);
                        p2WinButton.setChecked(p2IsClicked);
                        p2WinButton.setEnabled(!p2IsClicked);

                        p1WinButton.setOnClickListener(view -> {
                            p2WinButton.setChecked(false);
                            p2WinButton.setEnabled(true);

                            p1WinButton.setEnabled(false);

                            reportGameController.execute(position + 1, 1);
                            updateScore();
                        });
                        p2WinButton.setOnClickListener(view -> {
                            p1WinButton.setChecked(false);
                            p1WinButton.setEnabled(true);

                            p2WinButton.setEnabled(false);

                            reportGameController.execute(position + 1, 2);
                            updateScore();
                        });

                        // Create p1 character list
                        final Spinner p1CharSelect = convertView.findViewById(R.id.p1_char_select);
                        final List<String> possibleChars = new ArrayList<>(
                                EventData.getEventData().getCharacterIds().keySet());
                        possibleChars.add(0, "No Character");

                        ArrayAdapter<String> chars1Adapter = new ArrayAdapter<>(mContext,
                                android.R.layout.simple_spinner_dropdown_item, possibleChars);
                        chars1Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        p1CharSelect.setAdapter(chars1Adapter);

                        // Set the selected character in the icon to the one stored in state
                        final String p1SelectedChar = currentState.getCurrentSet().
                                getGame(position + 1).getPlayer1Character();
                        p1CharSelect.setSelection(possibleChars.indexOf(p1SelectedChar));

                        p1CharSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                                final String newP1Char = parent.getItemAtPosition(i).toString();
                                currentState.getCurrentSet().getGame(position + 1)
                                            .setPlayer1Character(newP1Char);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                                // When nothing is selected, the spinner should just stay on its value as before
                            }
                        });

                        // Create p2 character list
                        final Spinner p2CharSelect = convertView.findViewById(R.id.p2_char_select);
                        final ArrayAdapter<String> chars2Adapter = new ArrayAdapter<>(mContext,
                                android.R.layout.simple_spinner_dropdown_item, new ArrayList<String>(possibleChars));
                        chars1Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        p2CharSelect.setAdapter(chars2Adapter);

                        // Set the selected character stored in state
                        final String p2SelectedChar = currentState.getCurrentSet()
                                .getGame(position + 1).getPlayer2Character();
                        p2CharSelect.setSelection(possibleChars.indexOf(p2SelectedChar));

                        p2CharSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                                final String newP2Char = parent.getItemAtPosition(i).toString();
                                currentState.getCurrentSet().getGame(position + 1)
                                        .setPlayer2Character(newP2Char);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                                // When nothing is selected, the spinner should just stay on its value as before
                            }
                        });

                        return convertView;
                    }
                };

        gamesView.setAdapter(gameAdapter);
    }

    /**
     * Adds the score to the display (0-0, 1-0, etc.).
     */
    private void updateScore() {

        final ReportSetState currentState = reportViewModel.getState();

        final TextView p1Score = binding.player1Score;
        p1Score.setText(String.valueOf(currentState.getP1Wins()));

        final TextView p2Score = binding.player2Score;
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
            case "reportgamesuccess":
                break;
            case "apicallerror": showToast("We can't reach Start.gg right now.");
                break;
            case "incompletesetinfo": showToast("The set information is not complete!");
                break;
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

    /**
     * Creates the Report to Start.gg button to the UI.
     */
    private void createMutateButton() {
        final Button mutateSetButton = binding.mutateSetButton;
        mutateSetButton.setOnClickListener(view -> reportSetController.execute(
                binding.isP1DQ.isChecked(), binding.isP2DQ.isChecked()));
    }

}
