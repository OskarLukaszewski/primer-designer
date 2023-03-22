package gui.main_window;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;
import java.io.InputStream;

public class PrimerDesigner extends Application {

    private static Stage mainStage;
    private static Image icon = null;

    @Override
    public void start(Stage stage) throws IOException {
        mainStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MainWindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        InputStream iconPath = PrimerDesigner.class.getResourceAsStream("icons/icon.png");
        if (iconPath != null) {
            Image ico = new Image(iconPath);
            icon = ico;
            stage.getIcons().add(ico);
        }
        stage.setOnCloseRequest(windowEvent -> Platform.exit());
        stage.setTitle("Primer Designer");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public static Stage getMainStage() {
        return mainStage;
    }

    public static Image getIcon() {
        return icon;
    }
}