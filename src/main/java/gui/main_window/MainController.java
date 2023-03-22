package gui.main_window;

import gui.additional_windows.AdvancedOptionsWindow;
import gui.additional_windows.ResultsWindow;
import gui.additional_windows.SequenceWindow;
import gui.utils.FormatTools;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.RadioButton;
import javafx.stage.FileChooser;
import functionalities.primers.services.DesignPrimersService;
import functionalities.sequence_editor.services.VisualizeSequenceService;

public class MainController {

    private AdvancedOptionsWindow advancedOptionsWindow;
    private ResultsWindow resultsWindow;
    private SequenceWindow sequenceWindow;

    @FXML
    private CheckBox forCheckBox, revCheckBox;

    @FXML
    private Button pathButton;

    @FXML
    private TextField forFromEntry, forToEntry, revFromEntry, revToEntry,
            singleValueEntry, fromRangeEntry, toRangeEntry, tmFromEntry, tmToEntry, pathEntry;

    @FXML
    private RadioButton rbSingleValue, rbRange;

    @FXML
    private void initialize() {
        FormatTools.setFormatter(this.forFromEntry, "^\\d{0,5}$");
        FormatTools.setFormatter(this.forToEntry, "^\\d{0,5}$");
        FormatTools.setFormatter(this.revFromEntry, "^\\d{0,5}$");
        FormatTools.setFormatter(this.revToEntry, "^\\d{0,5}$");
        FormatTools.setFormatter(this.singleValueEntry, "^\\d{0,2}$");
        FormatTools.setFormatter(this.fromRangeEntry, "^\\d{0,2}$");
        FormatTools.setFormatter(this.toRangeEntry, "^\\d{0,2}$");
        FormatTools.setFormatter(this.tmFromEntry, "(^\\d{1,2}\\.\\d{0,2}$)|(^\\d{0,2}$)");
        FormatTools.setFormatter(this.tmToEntry, "(^\\d{1,2}\\.\\d{0,2}$)|(^\\d{0,2}$)");
        this.advancedOptionsWindow = new AdvancedOptionsWindow(PrimerDesigner.getMainStage());
        this.resultsWindow = new ResultsWindow();
        this.sequenceWindow = new SequenceWindow();
    }

    public void forCheckBoxChange() {
        if (this.forCheckBox.isSelected()) {
            this.forFromEntry.setDisable(false);
            this.forToEntry.setDisable(false);
        }
        else {
            this.forFromEntry.setDisable(true);
            this.forToEntry.setDisable(true);
        }
    }

    public void revCheckBoxChange() {
        if (this.revCheckBox.isSelected()) {
            this.revFromEntry.setDisable(false);
            this.revToEntry.setDisable(false);
        }
        else {
            this.revFromEntry.setDisable(true);
            this.revToEntry.setDisable(true);
        }
    }

    public void rbSingleValueChange() {
        if (this.rbSingleValue.isSelected()) {
            this.fromRangeEntry.setDisable(true);
            this.toRangeEntry.setDisable(true);
            this.singleValueEntry.setDisable(false);
        } else {
            this.fromRangeEntry.setDisable(false);
            this.toRangeEntry.setDisable(false);
            this.singleValueEntry.setDisable(true);
        }
    }

    public void rbRangeChange() {
        if (this.rbRange.isSelected()) {
            this.singleValueEntry.setDisable(true);
            this.fromRangeEntry.setDisable(false);
            this.toRangeEntry.setDisable(false);
        } else {
            this.singleValueEntry.setDisable(false);
            this.fromRangeEntry.setDisable(true);
            this.toRangeEntry.setDisable(true);
        }
    }

    public void filePathButtonClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Define a path to a FASTA file");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("FASTA files", "*.fasta", "*.fna", "*.ffn", "*.faa", "*.frn", "*.fa", "*.fsa"),
                new FileChooser.ExtensionFilter("Text files", "*.txt"),
                new FileChooser.ExtensionFilter("All files", "*.*"));
        String path = fileChooser.showOpenDialog(this.pathButton.getScene().getWindow()).getPath();
        this.pathEntry.setText(path);
    }

    public void visualizeButtonClick() {
        final String filePath = this.pathEntry.getText();
        VisualizeSequenceService service = new VisualizeSequenceService(this.sequenceWindow, filePath);
        service.startService();
    }

    public void advancedOptionsButtonClick() {
        this.advancedOptionsWindow.openWindow();
    }

    public void designButtonClick() {
        final boolean[] toDesign = {this.forCheckBox.isSelected(), this.revCheckBox.isSelected()};
        final String filePath = this.pathEntry.getText();
        final boolean[] filters = this.advancedOptionsWindow.getFilters();
        if (!toDesign[0] && !toDesign[1]) return;
        final String[][] flankPositions = {
                {this.forFromEntry.getText(), this.forToEntry.getText()},
                {this.revFromEntry.getText(), this.revToEntry.getText()}};
        final String[] meltingTemp = {this.tmFromEntry.getText(), this.tmToEntry.getText()};
        final String[] primerLength = new String[2];
        if (this.rbSingleValue.isSelected()) {
            primerLength[0] = this.singleValueEntry.getText();
            primerLength[1] = this.singleValueEntry.getText();
        } else {
            primerLength[0] = this.fromRangeEntry.getText();
            primerLength[1] = this.toRangeEntry.getText();
        }
        final String[] contentOfGC = this.advancedOptionsWindow.getContentOfGC();
        final String distributionOfGC = this.advancedOptionsWindow.getDistributionOfGC();
        final String maxRepeatLength = this.advancedOptionsWindow.getMaxRepeatLength();

        DesignPrimersService service = new DesignPrimersService(
                filePath,
                primerLength,
                meltingTemp,
                maxRepeatLength,
                contentOfGC,
                distributionOfGC,
                flankPositions,
                filters,
                toDesign,
                this.resultsWindow
        );
        service.startService();
    }
}