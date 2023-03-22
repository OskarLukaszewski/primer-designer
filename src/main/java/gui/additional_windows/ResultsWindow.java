package gui.additional_windows;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Screen;
import javafx.stage.Stage;
import static gui.main_window.PrimerDesigner.getMainStage;
import gui.additional_windows.additional_controllers.ResultsMainController;
import static gui.main_window.PrimerDesigner.getIcon;

public class ResultsWindow {
    
    private ResultsMainController resultsMainController;
    private final Stage resultsStage;
    private final Stage mainStage;
    private boolean isInitialized;
    
    public ResultsWindow() {
        this.resultsStage = new Stage();
        this.resultsStage.setOnCloseRequest(windowEvent -> this.isInitialized = false);
        this.isInitialized = false;
        this.mainStage = getMainStage();
    }

    public void showResults(String[][] forPrimers, String[][] revPrimers, String id) {
        if (!this.isInitialized) {
            initializeWindow();
            this.isInitialized = true;
        }
        if (!this.resultsStage.isShowing()) {
            boolean tabCreated = this.resultsMainController.showResults(forPrimers, revPrimers, id);
            if (tabCreated) {
                setPosition();
                this.resultsStage.show();
            }
        } else {
            this.resultsMainController.showResults(forPrimers, revPrimers, id);
        }
    }

    public boolean isIdUnique(String newId) {
        if (!this.isInitialized) {
            return true;
        }
        else {
            return this.resultsMainController.isIdUnique(newId);
        }
    }

    public void setFocusOnTab(String id) {
        this.resultsStage.requestFocus();
        this.resultsMainController.setFocusOnTab(id);
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
        this.resultsStage.setX(positionX);
        this.resultsStage.setY(positionY);
    }

    private void initializeWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ResultsWindow.fxml"));
            this.resultsMainController = new ResultsMainController(this.resultsStage);
            loader.setController(this.resultsMainController);
            this.resultsStage.setScene(new Scene(loader.load()));
            this.resultsStage.getIcons().add(getIcon());
            this.resultsStage.setTitle("Results");
        } catch (Exception error) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Results cannot be opened");
            errorAlert.setContentText(error.getMessage());
            Stage stage = (Stage) errorAlert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(getIcon());
            errorAlert.showAndWait();
        }
    }
}
