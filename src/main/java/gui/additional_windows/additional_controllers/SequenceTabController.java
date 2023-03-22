package gui.additional_windows.additional_controllers;

import gui.custom_elements.MyHTMLEditor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import functionalities.sequence_editor.services.SaveSequenceService;

import java.io.File;

public class SequenceTabController {

    @FXML
    private MyHTMLEditor textArea;

    private String filePath;

    public SequenceTabController() {
        this.filePath = "";
    }

    public String getLineLength() {
        return this.textArea.getLineLength();
    }

    public String getFontSize() {
        return this.textArea.getFontSize();
    }

    public void insertSequence(String[] sequence) {
        this.textArea.insertSequence(sequence);
    }

    public void setToBold() {
        this.textArea.setToBold();
    }

    public void setToItalic() {
        this.textArea.setToItalic();
    }

    public void setToUnderline() {
        this.textArea.setToUnderline();
    }

    public void changeTextColor(Color color, ActionEvent event) {
        this.textArea.setColor(color, event);
    }

    public void changeFontSize(String fontSize) {
        this.textArea.setFontSize(fontSize);
    }

    public void changeLineLength(int lineLength) {
        this.textArea.changeLineLength(lineLength);
    }

    public void undo() {
        this.textArea.undo();
    }

    public void redo() {
        this.textArea.redo();
    }

    public void copyText() {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putHtml(this.textArea.getNormalizedHtml());
        clipboard.setContent(content);
        System.out.println(this.textArea.getNormalizedHtml());
    }

    public void saveTextToFile(Stage stage) {
        File file = new File(this.filePath);
        if (file.exists()) {
            save();
        } else {
            saveAs(stage);
        }
    }

    public boolean isFilePathSet() {
        return !this.filePath.equals("");
    }

    public void saveAs(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Rich Text File", "*.rtf"),
                new FileChooser.ExtensionFilter("HTML Document", "*.html"));
        String path = fileChooser.showSaveDialog(stage).getPath();
        this.filePath = path;
        SaveSequenceService service = new SaveSequenceService(path, this.textArea.getNormalizedHtml());
        service.startService();
    }

    private void save() {
        SaveSequenceService service = new SaveSequenceService(this.filePath, this.textArea.getNormalizedHtml());
        service.startService();
    }
}