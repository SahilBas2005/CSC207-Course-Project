package com.example.csc207courseproject.ui.analysis;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.csc207courseproject.interface_adapter.ViewModel;
import com.example.csc207courseproject.interface_adapter.tournament_description.AnalysisState;

public class AnalysisViewModel extends ViewModel<AnalysisState> {

    public AnalysisViewModel() {
        super("analysis");
        setState(new AnalysisState());
    }
}