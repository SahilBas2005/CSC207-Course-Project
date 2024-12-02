package com.example.csc207courseproject.use_case.finance;
import java.io.File;
import java.util.List;

/**
 * Input Boundary for actions which are related to finance.
 */

public interface FinanceInputBoundary {

    List<String> getAllFinanceEntries(FinanceInputData inputData);
    void modifyPaymentStatus(FinanceInputData inputData);
    void exportToCSV(FinanceInputData inputData);
}
