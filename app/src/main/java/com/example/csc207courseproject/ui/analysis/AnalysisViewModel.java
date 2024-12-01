package com.example.csc207courseproject.ui.analysis;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.csc207courseproject.interface_adapter.ViewModel;
import com.example.csc207courseproject.interface_adapter.tournament_description.TournamentState;

public class AnalysisViewModel extends ViewModel<TournamentState> {

    public AnalysisViewModel() {
        super("analysis");
        setState(new TournamentState());
    }
}