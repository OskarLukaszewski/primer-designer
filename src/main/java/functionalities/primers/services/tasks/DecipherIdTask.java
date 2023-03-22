package functionalities.primers.services.tasks;

import javafx.concurrent.Task;
import functionalities.primers.main_tools.SearchIdTools;

public class DecipherIdTask extends Task<String> {

    private final String searchId;

    public DecipherIdTask(String searchId) {
        this.searchId = searchId;
    }

    @Override
    protected String call() throws Exception{
        return SearchIdTools.decipherId(this.searchId);
    }
}
