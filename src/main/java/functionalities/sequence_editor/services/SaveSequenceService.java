package functionalities.sequence_editor.services;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import functionalities.sequence_editor.services.tasks.SaveSequenceTask;

import static gui.main_window.PrimerDesigner.getIcon;

public class SaveSequenceService extends Service<Integer> {

    private final String filePath;
    private final String htmlText;

    public SaveSequenceService(String filePath, String htmlText) {
        this.filePath = filePath;
        this.htmlText = htmlText;

        setOnFailed(e -> {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Error during saving");
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
    protected Task<Integer> createTask() {
        return new SaveSequenceTask(this.filePath, this.htmlText);
    }
}
