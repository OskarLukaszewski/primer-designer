package functionalities.sequence_editor.services;

import functionalities.sequence_editor.services.tasks.CheckIfStillWiritingTask;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import gui.custom_elements.MyHTMLEditor;

public class CheckIfStillWritingService extends Service<Boolean> {

    private final MyHTMLEditor editor;

    public CheckIfStillWritingService(MyHTMLEditor editor) {
        this.editor = editor;

        setOnSucceeded(workerStateEvent -> saveChanges());
    }

    public void startService() {
        if (isRunning()) {
            cancel();
        }
        reset();
        start();
    }

    public void stopService() {
        if (isRunning()) {
            cancel();
            saveChanges();
        }
    }

    private void saveChanges() {
        this.editor.saveIfChanges(this.editor.getCurrentState(), this.editor.getNewState(), false);
    }

    @Override
    protected Task<Boolean> createTask() {
        return new CheckIfStillWiritingTask();
    }
}
