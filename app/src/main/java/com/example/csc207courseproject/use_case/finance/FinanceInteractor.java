package com.example.csc207courseproject.use_case.finance;

import com.example.csc207courseproject.data_access.APIDataAccessObject;
import com.example.csc207courseproject.entities.Entrant;

public class FinanceInteractor implements FinanceInputBoundary {


    @Override
    public void execute(FinanceInputData financeInputData) {
        // need ti access the data here
        FinanceDataAccessInterface dataAccessObject = new APIDataAccessObject();
//        dataAccessObject.getParticipantPaymentStatus()
        // need to send the updated data to the output data (through the output data)
    }
}
