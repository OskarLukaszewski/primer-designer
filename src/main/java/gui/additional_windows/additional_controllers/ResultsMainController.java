package gui.additional_windows.additional_controllers;

import gui.main_window.PrimerDesigner;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.stage.Stage;
import functionalities.primers.services.DecipherIdService;

import java.io.IOException;

public class ResultsMainController {

    @FXML
    private TabPane tabPane;

    private static final int maxNumberOfTabs = 20;
    private final Stage resultsStage;
    private final String[] idList;
    private int nextEmptyIndex;
    private int numberOfOpenTabs;

    @FXML
    private void initialize() {
        this.tabPane.setTabDragPolicy(TabPane.TabDragPolicy.REORDER);

    }

    public ResultsMainController(Stage resultsStage) {
        this.resultsStage = resultsStage;
        this.idList = new String[maxNumberOfTabs];
        this.nextEmptyIndex = 0;
        this.numberOfOpenTabs = 0;
    }

    public boolean showResults(String[][] forPrimers, String[][] revPrimers, String id) {
        getNextEmptyIndex();
        boolean isTabCreated = createTab(forPrimers, revPrimers, id);
        return isTabCreated;
    }

    public boolean isIdUnique(String newId) {
        for (String s : idList) {
            if (s == null) continue;
            if (s.equals(newId)) return false;
        }
        return true;
    }

    public void setFocusOnTab(String id) {
        for(Tab tab: this.tabPane.getTabs()) {
            String tabId = tab.getId();
            if (tabId.equals(id)) {
                this.tabPane.getSelectionModel().select(tab);
                return;
            }
        }
    }

    private void getNextEmptyIndex() {
        for (int i=0; i<maxNumberOfTabs; i++) {
            if (this.idList[i] == null) {
                this.nextEmptyIndex = i;
                return;
            }
        }
    }

    private boolean createTab(String[][] forPrimers, String[][] revPrimers, String id){
        if (this.numberOfOpenTabs==maxNumberOfTabs) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Max amount of tabs");
            alert.setContentText("Maximum amount of tabs has been reached ("+maxNumberOfTabs + ").");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(PrimerDesigner.getIcon());
            alert.showAndWait();
            return false;
        } else {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("ResultsTab.fxml"));
                ResultsTabController resultsTabController = new ResultsTabController();
                loader.setController(resultsTabController);
                Tab tab = loader.load();
                resultsTabController.insertForPrimers(forPrimers);
                resultsTabController.insertRevPrimers(revPrimers);
                tab.setText("Primers #"+(this.nextEmptyIndex+1));
                tab.setId(id);
                tab.setOnCloseRequest(event -> onClosingTab((Tab)event.getSource()));
                setContextMenu(tab);
                this.tabPane.getTabs().add(tab);
                this.tabPane.getSelectionModel().select(tab);
            } catch (IOException error){
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("Results cannot be opened");
                errorAlert.setContentText(error.getMessage());
                Stage stage = (Stage) errorAlert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(PrimerDesigner.getIcon());
                errorAlert.showAndWait();
                return false;
            }
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
            this.resultsStage.hide();
        }
    }

    private void setContextMenu(Tab tab) {
        ContextMenu menu = new ContextMenu();
        MenuItem button = new MenuItem("Show search parameters");
        String tabId = tab.getId();
        button.setOnAction(actionEvent -> {
            double[] position = {menu.getX(), menu.getY()};
            DecipherIdService service = new DecipherIdService(tabId, tab.getText(), position);
            service.startService();
        });
        menu.getItems().add(button);
        tab.setContextMenu(menu);
    }
}