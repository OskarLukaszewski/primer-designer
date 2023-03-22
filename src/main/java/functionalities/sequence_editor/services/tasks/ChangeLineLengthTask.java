package functionalities.sequence_editor.services.tasks;

import javafx.concurrent.Task;
import functionalities.sequence_editor.main_functions.ChangeLineLength;

public class ChangeLineLengthTask extends Task<String> {

    private final String htmlText;
    private final int newLineLength;

    public ChangeLineLengthTask(String htmlText ,int newLineLength) {
        this.htmlText = htmlText;
        this.newLineLength = newLineLength;
    }

    @Override
    protected String call() throws Exception {
        return ChangeLineLength.run(htmlText, this.newLineLength);
    }
}
