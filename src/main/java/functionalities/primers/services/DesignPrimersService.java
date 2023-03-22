package functionalities.primers.services;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import gui.additional_windows.ResultsWindow;
import functionalities.primers.services.tasks.DesignPrimersTask;
import static functionalities.primers.main_tools.SearchIdTools.getSearchId;

import static gui.main_window.PrimerDesigner.getIcon;

public class DesignPrimersService extends Service<String[][][][]> {

    private final String filePath, GCdistributionString, maxRepeatLengthString;
    private final String[] primerLengthString, meltingTempString, GCcontentString;
    private final String[][] flankPositionsString;
    private final boolean[] filters, toDesign;
    private final ResultsWindow resultsWindow;
    private String searchId;

    public DesignPrimersService(
            String filePath,
            String[] primerLengthString,
            String[] meltingTempString,
            String maxRepeatLengthString,
            String[] GCcontentString,
            String GCdistributionString,
            String[][] flankPositionsString,
            boolean[] filters,
            boolean[] toDesign,
            ResultsWindow resultsWindow) {

        this.filePath = filePath;
        this.primerLengthString = primerLengthString;
        this.meltingTempString = meltingTempString;
        this.maxRepeatLengthString = maxRepeatLengthString;
        this.GCcontentString = GCcontentString;
        this.GCdistributionString = GCdistributionString;
        this.flankPositionsString = flankPositionsString;
        this.filters = filters;
        this.toDesign = toDesign;
        this.resultsWindow = resultsWindow;

        setOnSucceeded(e -> showResults());

        setOnFailed(e -> {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Error during designing primers");
            errorAlert.setContentText(getException().getMessage());
            Stage stage = (Stage) errorAlert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(getIcon());
            errorAlert.showAndWait();
        });
    }

    public void startService() {
        if (isRunning()) return;
        getId();
        if (!this.resultsWindow.isIdUnique(this.searchId)) {
            this.resultsWindow.setFocusOnTab(this.searchId);
            return;
        }
        reset();
        start();
    }

    @Override
    protected Task<String[][][][]> createTask() {
        return new DesignPrimersTask(
                this.filePath,
                this.primerLengthString,
                this.meltingTempString,
                this.maxRepeatLengthString,
                this.GCcontentString,
                this.GCdistributionString,
                this.flankPositionsString,
                this.filters,
                this.toDesign
        );
    }

    private void showResults() {
        String[][][][] analyzedPrimers = getValue();
        if (doPrimersExist(analyzedPrimers)) {
            for (String[][][] primers : analyzedPrimers) {
                resultsWindow.showResults(primers[0], primers[1], this.searchId);
            }
            reset();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Empty output");
            alert.setContentText("No primers with provided parameters have been found.");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(getIcon());
            alert.showAndWait();
            reset();
        }
    }

    private boolean doPrimersExist(String[][][][] analyzedPrimers) {
        int numberOfEmptySearchers = 0;
        for (String[][][] primers: analyzedPrimers) {
            if (primers[0].length == 0 && primers[1].length == 0) {
                numberOfEmptySearchers++;
            }
        }
        return numberOfEmptySearchers != analyzedPrimers.length;
    }

    private void getId() {
        this.searchId = getSearchId(this.filePath,
                this.primerLengthString,
                this.meltingTempString,
                this.maxRepeatLengthString,
                this.GCcontentString,
                this.GCdistributionString,
                this.flankPositionsString,
                this.filters,
                this.toDesign);
    }
}
