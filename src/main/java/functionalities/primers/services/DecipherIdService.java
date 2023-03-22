package functionalities.primers.services;

import functionalities.primers.services.tasks.DecipherIdTask;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.layout.Region;
import javafx.stage.Screen;
import javafx.stage.Stage;

import static gui.main_window.PrimerDesigner.getIcon;

public class DecipherIdService extends Service<String> {

    private final String searchId;
    private final String tabText;
    private final double[] windowPosition;

    public DecipherIdService(String searchId, String tabText, double[] windowPosition) {

        this.searchId = searchId;
        this.tabText = tabText;
        this.windowPosition = windowPosition;

        setOnSucceeded(workerStateEvent -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Search Parameters For " + this.tabText);
            alert.setContentText(getValue());
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.getDialogPane().setMinWidth(Region.USE_PREF_SIZE);
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(getIcon());
            double screenWidth = Screen.getPrimary().getBounds().getWidth();
            double windowWidth = alert.getDialogPane().getWidth();
            if (this.windowPosition[0]+windowWidth<=screenWidth) {
                stage.setX(this.windowPosition[0]-10);
            } else {
                stage.setX(screenWidth-windowWidth-10);
            }
            stage.setY(this.windowPosition[1]);
            stage.show();
        });

        setOnFailed(workerStateEvent -> {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Error during deciphering information");
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
    protected Task<String> createTask() {
        return new DecipherIdTask(this.searchId);
    }
}
