package use_case.report_set;

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
