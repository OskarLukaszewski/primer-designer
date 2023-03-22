package gui.additional_windows.additional_controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.io.IOException;
import static gui.utils.FormatTools.setFormatter;
import static gui.main_window.PrimerDesigner.getIcon;

public class SequenceMainController {

    @FXML
    private TabPane tabPane;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private Rectangle colorRectangle;
    @FXML
    private Button changeColorButton, saveButton;
    @FXML
    private TextField lineLengthEntry, fontSizeEntry;

    private static final int maxNumberOfTabs = 20;
    private final Stage sequenceStage;
    private final String[] idList;
    private final SequenceTabController[] controllerList;
    private int nextEmptyIndex;
    private int numberOfOpenTabs;

    @FXML
    private void initialize() {
        this.tabPane.setTabDragPolicy(TabPane.TabDragPolicy.REORDER);
        this.colorPicker.getStyleClass().add("button");
        setFormatter(this.lineLengthEntry, "^\\d{0,3}$");
        setFormatter(this.fontSizeEntry, "^\\d{0,2}$");
        this.fontSizeEntry.setText("9");
        ContextMenu menu = new ContextMenu();
        final MenuItem save = new MenuItem("save");
        save.setOnAction(actionEvent -> saveButtonClick());
        final MenuItem saveAs = new MenuItem("save as");
        saveAs.setOnAction(actionEvent -> saveAs());
        menu.getItems().add(saveAs);
        menu.getItems().add(save);
        menu.setOnShowing(windowEvent -> save.setDisable(!getControllerOfSelectedTab().isFilePathSet()));
        this.saveButton.setContextMenu(menu);
        this.fontSizeEntry.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (!t1) {
                setFontSizeEntryText(getControllerOfSelectedTab().getFontSize());
            } else {
                setFontSizeEntryText("");
            }
        });
        this.lineLengthEntry.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (!t1) {
                setLineLengthEntryText(getControllerOfSelectedTab().getLineLength());
            } else {
                setLineLengthEntryText("");
            }
        });
    }

    public SequenceMainController(Stage sequenceStage) {
        this.sequenceStage = sequenceStage;
        this.idList = new String[maxNumberOfTabs];
        this.controllerList = new SequenceTabController[maxNumberOfTabs];
        this.nextEmptyIndex = 0;
        this.numberOfOpenTabs = 0;
    }

    public void colorPickerClick(ActionEvent event) {
        this.colorRectangle.setFill(this.colorPicker.getValue());
        changeColorButtonClick(event);
    }

    public void changeColorButtonClick(ActionEvent event) {
        getControllerOfSelectedTab().changeTextColor(this.colorPicker.getValue(), event);
    }

    public void undoButtonClick() {
        SequenceTabController sequenceTabController = getControllerOfSelectedTab();
        sequenceTabController.undo();
        this.fontSizeEntry.setText(sequenceTabController.getFontSize());
        this.lineLengthEntry.setText(sequenceTabController.getLineLength());
    }

    public void redoButtonClick() {
        SequenceTabController sequenceTabController = getControllerOfSelectedTab();
        sequenceTabController.redo();
        this.fontSizeEntry.setText(sequenceTabController.getFontSize());
        this.lineLengthEntry.setText(sequenceTabController.getLineLength());
    }

    public void copyButtonClick() {
        getControllerOfSelectedTab().copyText();
    }

    public void boldButtonClick() {
        getControllerOfSelectedTab().setToBold();
    }

    public void italicButtonClick() {
        getControllerOfSelectedTab().setToItalic();
    }

    public void underlineButtonClick() {
        getControllerOfSelectedTab().setToUnderline();
    }

    public void saveButtonClick() {
        getControllerOfSelectedTab().saveTextToFile(this.sequenceStage);
    }

    public void lineLengthEntryKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            getControllerOfSelectedTab().changeLineLength(Integer.parseInt(this.lineLengthEntry.getText()));
        }
    }

    public void fontSizeEntryKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            getControllerOfSelectedTab().changeFontSize(this.fontSizeEntry.getText());
        }
    }

    public void setColorPicker() {
        Rectangle rec = (Rectangle) this.colorPicker.lookup("Rectangle");
        rec.setVisible(false);
    }

    public boolean showResults(String[] convertedSequence) {
        getNextEmptyIndex();
        boolean isTabCreated = createTab(convertedSequence);
        return isTabCreated;
    }

    private void saveAs() {
        getControllerOfSelectedTab().saveAs(this.sequenceStage);
    }

    private void setLineLengthEntryText(String text) {
        this.lineLengthEntry.setText(text);
    }

    private void setFontSizeEntryText(String text) {
        this.fontSizeEntry.setText(text);
    }

    private void getNextEmptyIndex() {
        for (int i=0; i<maxNumberOfTabs; i++) {
            if (this.idList[i] == null) {
                this.nextEmptyIndex = i;
                return;
            }
        }
    }

    private SequenceTabController getControllerOfSelectedTab() {
        String id = this.tabPane.getSelectionModel().getSelectedItem().getId();
        return this.controllerList[Integer.parseInt(id)-1];
    }

    private boolean createTab(String[] convertedSequence){
        if (this.numberOfOpenTabs==maxNumberOfTabs) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Max amount of tabs");
            alert.setContentText("Maximum amount of tabs has been reached ("+maxNumberOfTabs + ").");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(getIcon());
            alert.showAndWait();
            return false;
        } else {
            SequenceTabController sequenceTabController = new SequenceTabController();
            String id = String.valueOf(this.nextEmptyIndex+1);
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("SequenceTab.fxml"));
                loader.setController(sequenceTabController);
                final Tab tab = loader.load();
                sequenceTabController.insertSequence(convertedSequence);
                tab.setText("Sequence #"+id);
                tab.setId(id);
                tab.setOnCloseRequest(event -> onClosingTab((Tab)event.getSource()));
                tab.setOnSelectionChanged(event -> {
                    if (tab.isSelected()) {
                        setLineLengthEntryText(sequenceTabController.getLineLength());
                        setFontSizeEntryText(sequenceTabController.getFontSize());
                    }
                });
                this.tabPane.getTabs().add(tab);
                this.tabPane.getSelectionModel().select(tab);
            } catch (IOException error) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("Results cannot be opened");
                errorAlert.setContentText(error.getMessage());
                Stage stage = (Stage) errorAlert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(getIcon());
                errorAlert.showAndWait();
                return false;
            }
            this.controllerList[this.nextEmptyIndex] = sequenceTabController;
            this.idList[this.nextEmptyIndex] = id;
            this.numberOfOpenTabs++;
            return true;
        }
    }

    private void onClosingTab(Tab tab) {
        String tabId = tab.getId();
        for (int i=0; i<this.idList.length; i++) {
            if (tabId.equals(this.idList[i])) {
                this.idList[i] = null;
                this.numberOfOpenTabs--;
                break;
            }
        }
        if (this.numberOfOpenTabs == 0) {
            this.sequenceStage.hide();
        }
    }
}
