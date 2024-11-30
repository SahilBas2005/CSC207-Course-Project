package com.example.csc207courseproject.use_case.finance;

/**
 * Input Boundary for actions which are related to finance.
 */

public interface FinanceInputBoundary {

    /**
     * Executes the finance use case.
     * @param financeInputData the input data
     */
   void execute(FinanceInputData financeInputData);

}
