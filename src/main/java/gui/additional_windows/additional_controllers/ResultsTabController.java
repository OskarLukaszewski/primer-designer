package gui.additional_windows.additional_controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class ResultsTabController {

    @FXML
    private TextArea forEntry, revEntry;

    public void insertForPrimers(String[][] primers) {
        StringBuilder output = new StringBuilder();
        for (int i=0; i<primers.length; i++) {
            String[] primer = primers[primers.length-1-i];
            output.append(i + 1).append(". ").append(primer[0]).append("\n");
            output.append("Tm: ").append(primer[1]).append(" | 5' position: ").append(primer[2]).append(" | L: ")
                    .append(primer[0].length()).append("\n\n");
        }
        if (output.length()>1) {
            output.setLength(output.length()-2);
        }
        this.forEntry.setText(output.toString());
    }

    public void insertRevPrimers(String[][] primers) {
        StringBuilder output = new StringBuilder();
        for (int i=0; i<primers.length; i++) {
            String[] primer = primers[primers.length-1-i];
            output.append(i + 1).append(". ").append(primer[0]).append("\n");
            output.append("Tm: ").append(primer[1]).append(" | 5' position: ").append(primer[2]).append(" | L: ")
                    .append(primer[0].length()).append("\n\n");
        }
        if (output.length()>1) {
            output.setLength(output.length()-2);
        }
        this.revEntry.setText(output.toString());
    }
}
