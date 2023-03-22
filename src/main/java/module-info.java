module main.primerdesigner {

    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires java.desktop;
    requires de.jensd.fx.glyphs.fontawesome;
    requires org.apache.commons.text;

    opens functionalities.sequence_editor.services to javafx.fxml;
    exports functionalities.sequence_editor.services;
    opens gui.additional_windows to javafx.fxml;
    exports gui.additional_windows;
    opens functionalities.primers.services.tasks to javafx.fxml;
    exports functionalities.primers.services.tasks;
    opens gui.additional_windows.additional_controllers to javafx.fxml;
    exports gui.additional_windows.additional_controllers;
    opens functionalities.primers.services to javafx.fxml;
    exports functionalities.primers.services;
    opens gui.main_window to javafx.fxml;
    exports gui.main_window;
    opens gui.custom_elements to javafx.fxml;
    exports gui.custom_elements;
}