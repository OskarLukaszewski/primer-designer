package functionalities.sequence_editor.services.tasks;

import javafx.concurrent.Task;

public class CheckIfStillWiritingTask extends Task<Boolean> {

    @Override
    protected Boolean call() throws Exception {

        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < 500) {
            if (isCancelled()) {
                throw new InterruptedException();
            }
        }

        return false;
    }
}
