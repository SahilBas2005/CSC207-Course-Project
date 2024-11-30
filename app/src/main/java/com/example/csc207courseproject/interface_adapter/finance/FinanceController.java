package com.example.csc207courseproject.interface_adapter.finance;

import com.example.csc207courseproject.use_case.finance.*;
/**
 * The controller for the Finance Use Case.
 */
public class FinanceController {

    private final FinanceInputBoundary financeUseCaseInteractor;

    public FinanceController(FinanceInputBoundary financeUseCaseInteractor) {
        this.financeUseCaseInteractor = financeUseCaseInteractor;
    }

    /**
     * Executes the Finance Use Case.
     * @param gameID the ID of the game for which the financial data is being fetched
     * @param slug the slug representing the tournament or event
     */
    public void execute(String gameID, String slug) {
        final FinanceInputData financeInputData = new FinanceInputData(
                gameID, slug);

        financeUseCaseInteractor.execute(financeInputData);
    }
}
