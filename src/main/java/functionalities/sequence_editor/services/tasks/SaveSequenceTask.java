package functionalities.sequence_editor.services.tasks;

import functionalities.sequence_editor.main_functions.SaveSequence;
import javafx.concurrent.Task;

public class SaveSequenceTask extends Task<Integer> {

    private final String filePath;
    private final String htmlText;

    public SaveSequenceTask(String filePath, String htmlText) {
        this.filePath = filePath;
        this.htmlText = htmlText;
    }

    @Override
    protected Integer call() throws Exception {
        return SaveSequence.run(this.filePath, this.htmlText);
    }
}
