package gui.additional_windows;

import gui.additional_windows.additional_controllers.AdvancedOptionsController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import static gui.main_window.PrimerDesigner.getIcon;

public class AdvancedOptionsWindow {

    private final AdvancedOptionsController advancedOptionsController;
    private final Stage advancedOptionsStage;
    private final Stage mainStage;

    public AdvancedOptionsWindow(Stage mainStage) {
        this.advancedOptionsStage = new Stage();
        this.advancedOptionsStage.setTitle("Advanced Options");
        this.advancedOptionsStage.setResizable(false);
        this.advancedOptionsController = new AdvancedOptionsController(this.advancedOptionsStage);
        this.mainStage = mainStage;
    }

    public void openWindow() {
        if (this.advancedOptionsStage.isShowing()) {
            this.advancedOptionsStage.requestFocus();
        }
        else {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("AdvancedOptionsWindow.fxml"));
                loader.setController(advancedOptionsController);
                this.advancedOptionsStage.getIcons().add(getIcon());
                this.advancedOptionsStage.setScene(new Scene(loader.load()));
                this.advancedOptionsStage.setX(this.mainStage.getX()+100);
                this.advancedOptionsStage.setY(this.mainStage.getY()+65.5);
                this.advancedOptionsStage.show();
            } catch (Exception error) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("Advanced options cannot be opened");
                errorAlert.setContentText(error.getMessage());
                Stage stage = (Stage) errorAlert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(getIcon());
                errorAlert.showAndWait();
            }
        }
    }

    public boolean[] getFilters() {
        return this.advancedOptionsController.getFilters();
    }
    public String getMaxRepeatLength() {
        return this.advancedOptionsController.getMaxRepeatLength();
    }
    public String[] getContentOfGC() {
        return this.advancedOptionsController.getContentOfGC();
    }
    public String getDistributionOfGC() {
        return this.advancedOptionsController.getDistributionOfGC();
    }
}
