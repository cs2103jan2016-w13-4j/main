package jfdi.ui.commandhandlers;

import com.google.common.eventbus.Subscribe;

import jfdi.logic.events.SearchDoneEvent;
import jfdi.ui.Constants;
import jfdi.ui.Constants.ListStatus;
import jfdi.ui.Constants.MsgType;

public class SearchHandler  extends CommandHandler {
    
    private static SearchHandler instance = new SearchHandler();
    
    private SearchHandler() {
    }
    
    public static SearchHandler getInstance() {
        return instance;
    }
    
    @Subscribe
    public void handleSearchDoneEvent(SearchDoneEvent e) {
        if (controller.isInternalCall()) {
            // Add any method calls strictly for internal calls here
            return;
        }

        listTasks(e.getResults(), false);

        switchContext(ListStatus.SEARCH, false);

        for (String key : e.getKeywords()) {
            controller.searchCmd += key + " ";
        }

        // controller.switchTabSkin();
        // controller.setHighlights(e.getKeywords());
        controller.relayFb(Constants.CMD_SUCCESS_SEARCH, MsgType.SUCCESS);
        controller.updateNotiBubbles();
    }
}
