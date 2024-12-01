package com.example.csc207courseproject.ui.call;

import com.example.csc207courseproject.interface_adapter.ViewModel;
import com.example.csc207courseproject.interface_adapter.call_set.CallSetState;

public class CallViewModel extends ViewModel<CallSetState> {


    public CallViewModel() {
        super("call");
        setState(new CallSetState());
    }
}