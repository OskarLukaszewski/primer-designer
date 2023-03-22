package functionalities.sequence_editor.services.tasks;

import functionalities.sequence_editor.main_functions.ProcessFastaFile;
import javafx.concurrent.Task;

public class VisualizeSequenceTask extends Task<String[][]> {

    private final String path;

    public VisualizeSequenceTask(String path) {
        this.path = path;
    }

    @Override
    protected String[][] call() throws Exception {
        return ProcessFastaFile.run(this.path);
    }
}
