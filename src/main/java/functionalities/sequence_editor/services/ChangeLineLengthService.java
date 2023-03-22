package functionalities.sequence_editor.services;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import functionalities.sequence_editor.services.tasks.ChangeLineLengthTask;
import gui.custom_elements.MyHTMLEditor;

import static gui.main_window.PrimerDesigner.getIcon;

public class ChangeLineLengthService extends Service<String> {

    private final String htmlText;
    private final int newLineLength;
    private final MyHTMLEditor htmlEditor;

    public ChangeLineLengthService(String htmlText, int newLineLength, MyHTMLEditor myHTMLEditor) {
        this.htmlText = htmlText;
        this.newLineLength = newLineLength;
        this.htmlEditor = myHTMLEditor;

        setOnSucceeded(e -> insertSequence(getValue()));

        setOnFailed(e -> {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Error during changing the length of line");
            errorAlert.setContentText(getException().getMessage());
            Stage stage = (Stage) errorAlert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(getIcon());
            errorAlert.showAndWait();
        });
    }

    public void startService() {
        if(isRunning()) return;
        reset();
        start();
    }

    @Override
    protected Task<String> createTask() {
        return new ChangeLineLengthTask(this.htmlText, this.newLineLength);
    }

    private void insertSequence(String newHtmlText) {
        String[] oldState = this.htmlEditor.getCurrentState();
        float scrollPosition = this.htmlEditor.getScroll();
        this.htmlEditor.setHtmlText(newHtmlText);
        this.htmlEditor.setScroll(scrollPosition);
        this.htmlEditor.setLineLength(String.valueOf(this.newLineLength));
        String[] newState = this.htmlEditor.getNewState();
        this.htmlEditor.saveIfChanges(oldState, newState, true);
    }
}
