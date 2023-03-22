package gui.additional_windows.additional_controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import static gui.utils.FormatTools.setFormatter;

public class AdvancedOptionsController {

    private final Stage advancedOptionsStage;

    private String numberOfRepeats, fromRangeOfGC, toRangeOfGC, distributionOfGC;

    public boolean isAnalyzeEnd, isAnalyzeComplementation, isAnalyzeRepeats, isAnalyzeContentOfGC, isAnalyzeDistributionOfGC;

    @FXML
    private CheckBox analyzeEnd, analyzeComplementation, analyzeRepeats, analyzeContentOfGC, analyzeDistributionOfGC;

    @FXML
    private TextField repeatsEntry, fromRangeOfGCEntry, toRangeOfGCEntry, distributionOfGCEntry;

    @FXML
    private Button saveButton, cancelButton;

    @FXML
    private void initialize() {
        setFormatter(this.repeatsEntry, "^\\d{0,2}$");
        setFormatter(this.fromRangeOfGCEntry, "(^\\d{0,2}\\.\\d{0,2}$)|(^\\d{0,5}$)|(^100\\.0{0,2}$)|(^100$)");
        setFormatter(this.toRangeOfGCEntry, "(^\\d{0,2}\\.\\d{0,2}$)|(^\\d{0,5}$)|(^100\\.0{0,2}$)|(^100$)");
        setFormatter(this.distributionOfGCEntry, "(^\\d{0,2}\\.\\d{0,2}$)|(^\\d{0,5}$)|(^100\\.0{0,2}$)|(^100$)");
        setSelection();
        setText();
    }

    public AdvancedOptionsController(Stage advancedOptionsStage) {
        this.numberOfRepeats = "3";
        this.fromRangeOfGC = "40.00";
        this.toRangeOfGC = "60.00";
        this.distributionOfGC = "80.00";
        this.isAnalyzeEnd = true;
        this.isAnalyzeComplementation = true;
        this.isAnalyzeRepeats = true;
        this.isAnalyzeContentOfGC = true;
        this.isAnalyzeDistributionOfGC = true;
        this.advancedOptionsStage = advancedOptionsStage;
    }

    public boolean[] getFilters() {
        return new boolean[]{
                this.isAnalyzeEnd,
                this.isAnalyzeComplementation,
                this.isAnalyzeRepeats,
                this.isAnalyzeContentOfGC,
                this.isAnalyzeDistributionOfGC
        };
    }

    public String getMaxRepeatLength() {
        return this.numberOfRepeats;
    }

    public String[] getContentOfGC() {
        return new String[] {
                this.fromRangeOfGC,
                this.toRangeOfGC
        };
    }

    public String getDistributionOfGC() {
        return this.distributionOfGC;
    }

    public void defaultButtonClick() {
        this.repeatsEntry.setText("3");
        this.fromRangeOfGCEntry.setText("40.00");
        this.toRangeOfGCEntry.setText("60.00");
        this.distributionOfGCEntry.setText("80.00");
        this.analyzeEnd.setSelected(true);
        this.analyzeComplementation.setSelected(true);
        this.analyzeRepeats.setSelected(true);
        this.repeatsEntry.setDisable(false);
        this.analyzeContentOfGC.setSelected(true);
        this.fromRangeOfGCEntry.setDisable(false);
        this.toRangeOfGCEntry.setDisable(false);
        this.analyzeDistributionOfGC.setSelected(true);
        this.distributionOfGCEntry.setDisable(false);
    }

    public void saveButtonClick() {
        this.numberOfRepeats = this.repeatsEntry.getText();
        this.fromRangeOfGC = this.fromRangeOfGCEntry.getText();
        this.toRangeOfGC = this.toRangeOfGCEntry.getText();
        this.distributionOfGC = this.distributionOfGCEntry.getText();
        this.isAnalyzeEnd = this.analyzeEnd.isSelected();
        this.isAnalyzeComplementation = this.analyzeComplementation.isSelected();
        this.isAnalyzeRepeats = this.analyzeRepeats.isSelected();
        this.isAnalyzeContentOfGC = this.analyzeContentOfGC.isSelected();
        this.isAnalyzeDistributionOfGC = this.analyzeDistributionOfGC.isSelected();
        this.advancedOptionsStage.close();
    }

    public void cancelButtonClick() {
        this.advancedOptionsStage.close();
    }

    public void analyzeRepeatsChange() {
        this.repeatsEntry.setDisable(!this.analyzeRepeats.isSelected());
    }

    public void analyzeContentOfGCChange() {
        if (this.analyzeContentOfGC.isSelected()) {
            this.fromRangeOfGCEntry.setDisable(false);
            this.toRangeOfGCEntry.setDisable(false);
        } else {
            this.fromRangeOfGCEntry.setDisable(true);
            this.toRangeOfGCEntry.setDisable(true);
        }
    }

    public void analyzeDistributionOfGCChange() {
        this.distributionOfGCEntry.setDisable(!this.analyzeDistributionOfGC.isSelected());
    }

    private void setSelection() {
        
        this.analyzeEnd.setSelected(isAnalyzeEnd);

        this.analyzeComplementation.setSelected(isAnalyzeComplementation);

        if (isAnalyzeRepeats) {
            this.analyzeRepeats.setSelected(true);
            this.repeatsEntry.setDisable(false);
        } else {
            this.analyzeRepeats.setSelected(false);
            this.repeatsEntry.setDisable(true);
        }

        if (isAnalyzeContentOfGC) {
            this.analyzeContentOfGC.setSelected(true);
            this.fromRangeOfGCEntry.setDisable(false);
            this.toRangeOfGCEntry.setDisable(false);
        } else {
            this.analyzeContentOfGC.setSelected(false);
            this.fromRangeOfGCEntry.setDisable(true);
            this.toRangeOfGCEntry.setDisable(true);
        }

        if (isAnalyzeDistributionOfGC) {
            this.analyzeDistributionOfGC.setSelected(true);
            this.distributionOfGCEntry.setDisable(false);
        } else {
            this.analyzeDistributionOfGC.setSelected(false);
            this.distributionOfGCEntry.setDisable(true);
        }
    }

    private void setText() {
        this.repeatsEntry.setText(this.numberOfRepeats);
        this.fromRangeOfGCEntry.setText(this.fromRangeOfGC);
        this.toRangeOfGCEntry.setText(this.toRangeOfGC);
        this.distributionOfGCEntry.setText(this.distributionOfGC);
    }
}