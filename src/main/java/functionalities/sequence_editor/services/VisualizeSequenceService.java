package functionalities.sequence_editor.services;

import functionalities.sequence_editor.services.tasks.VisualizeSequenceTask;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import gui.additional_windows.SequenceWindow;

import static gui.main_window.PrimerDesigner.getIcon;

public class VisualizeSequenceService extends Service<String[][]> {

    private final String path;
    private final SequenceWindow sequenceWindow;

    public VisualizeSequenceService(SequenceWindow sequenceWindow, String path) {
        this.path = path;
        this.sequenceWindow = sequenceWindow;

        setOnSucceeded(e -> showResults());

        setOnFailed(workerStateEvent -> {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Error during visualizing sequence");
            errorAlert.setContentText(getException().getMessage());
            Stage stage = (Stage) errorAlert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(getIcon());
            errorAlert.showAndWait();
        });
    }

    public void startService() {
        if (isRunning()) return;
        reset();
        start();
    }

    @Override
    protected Task<String[][]> createTask() {
        return new VisualizeSequenceTask(this.path);
    }

    private void showResults() {
        String[][] convertedSequences = getValue();
        if (doSequencesExist(convertedSequences)) {
            for (int i=0; i<convertedSequences.length; i++) {
                this.sequenceWindow.showResults(convertedSequences[i]);
            }
            reset();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Empty output");
            alert.setContentText("No sequences have been found.");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(getIcon());
            alert.showAndWait();
            reset();
        }
    }

    private boolean doSequencesExist(String[][] convertedSequences) {
        int numberOfEmptySequences = 0;
        for (String[] sequence: convertedSequences) {
            if (sequence.length == 0) {
                numberOfEmptySequences++;
            }
        }
        return numberOfEmptySequences != convertedSequences.length;
    }
}
