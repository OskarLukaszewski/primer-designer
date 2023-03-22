package gui.additional_windows;

import gui.additional_windows.additional_controllers.SequenceMainController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import static gui.main_window.PrimerDesigner.getIcon;
import javafx.stage.Screen;
import javafx.stage.Stage;
import static gui.main_window.PrimerDesigner.getMainStage;

public class SequenceWindow {

    private SequenceMainController sequenceMainController;
    private final Stage sequenceStage;
    private final Stage mainStage;
    private boolean isInitialized;

    public SequenceWindow() {
        this.sequenceStage = new Stage();
        this.sequenceStage.setOnCloseRequest(windowEvent -> this.isInitialized = false);
        this.isInitialized = false;
        this.mainStage = getMainStage();
    }

    public void showResults(String[] convertedSequence) {
        if (!this.isInitialized) {
            initializeWindow();
            this.isInitialized = true;
        }
        if (!this.sequenceStage.isShowing()) {
            boolean tabCreated = this.sequenceMainController.showResults(convertedSequence);
            if (tabCreated) {
                setPosition();
                this.sequenceStage.show();
                this.sequenceMainController.setColorPicker();
            }
        } else {
            this.sequenceMainController.showResults(convertedSequence);
            this.sequenceStage.hide();
            this.sequenceStage.show();
        }
    }

    private void setPosition() {
        double screenWidth = Screen.getPrimary().getBounds().getWidth();
        double positionX;
        double positionY;
        if (this.mainStage.getX()+534+600<=screenWidth) {
            positionX = this.mainStage.getX() + 534;
            positionY = this.mainStage.getY();
        } else if (this.mainStage.getX()-602>=0) {
            positionX = this.mainStage.getX() - 602;
            positionY = this.mainStage.getY();
        } else return;
        this.sequenceStage.setX(positionX);
        this.sequenceStage.setY(positionY);
    }

    private void initializeWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SequenceWindow.fxml"));
            this.sequenceMainController = new SequenceMainController(this.sequenceStage);
            loader.setController(this.sequenceMainController);
            Scene scene = new Scene(loader.load());
            this.sequenceStage.setScene(scene);
            this.sequenceStage.getIcons().add(getIcon());
            this.sequenceStage.setTitle("Sequences");
        } catch (Exception error) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Sequence cannot be opened");
            errorAlert.setContentText(error.getMessage());
            Stage stage = (Stage) errorAlert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(getIcon());
            errorAlert.showAndWait();
        }
    }
}
