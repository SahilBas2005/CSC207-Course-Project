
package com.example.csc207courseproject.use_case.report_set;

/**
 * The OutputBoundary for the Report Set use case.
 */
public interface ReportSetOutputBoundary {

    /**
     * Prepares the success view for the Report Set Use Case.
     */
    void prepareSuccessView();

    /**
     * Prepares the failure view for the Report Set Use Case.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);

}
