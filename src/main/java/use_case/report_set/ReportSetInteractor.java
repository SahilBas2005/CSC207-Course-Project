package use_case.report_set;

public class ReportSetInteractor implements ReportSetInputBoundary{
    private final ReportSetDataAccessInterface dataAccess;
    private final ReportSetOutputBoundary reportSetPresenter;

    public ReportSetInteractor(ReportSetDataAccessInterface dataAccess, ReportSetOutputBoundary reportSetPresenter) {
        this.dataAccess = dataAccess;
        this.reportSetPresenter = reportSetPresenter;
    }

    @Override
    public void execute(ReportSetInputData mutateSeedingInputData) {


        try {
            //dataAccess.reportSet();
            reportSetPresenter.prepareSuccessView();
        } catch (Exception e) {
            reportSetPresenter.prepareFailView("Something went wrong with the API call, try again.");
        }
    }

    // Add the switch to main view stuff???

}
