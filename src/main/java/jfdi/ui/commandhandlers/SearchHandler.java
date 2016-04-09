// @@author A0129538W

package jfdi.ui.commandhandlers;

import com.google.common.eventbus.Subscribe;

import jfdi.logic.events.SearchDoneEvent;
import jfdi.ui.Constants;
import jfdi.ui.Constants.ListStatus;
import jfdi.ui.Constants.MsgType;

public class SearchHandler extends CommandHandler {

    private static SearchHandler instance = new SearchHandler();

    private SearchHandler() {
    }

    public static SearchHandler getInstance() {
        return instance;
    }

    @Subscribe
    public void handleSearchDoneEvent(SearchDoneEvent e) {

        // check size of result and keyword > 0

        controller.switchContext(ListStatus.SEARCH, false);
        controller.listTasks(e.getResults(), true);
        controller.switchTabSkin();
        controller.updateNotiBubbles();
        createSearchFb(e);

    }

    private void createSearchFb(SearchDoneEvent e) {
        controller.searchCmd = Constants.CTRL_CMD_SEARCH;
        if (e.getKeywords().size() == 1) {
            controller.relayFb(String.format(Constants.CMD_SUCCESS_SEARCH_1, e.getKeywords()), MsgType.SUCCESS);
        } else {
            int count = 0;
            String searchKeyWords = "";
            for (String key : e.getKeywords()) {
                controller.searchCmd += key + " ";
                searchKeyWords += key;
                count++;
                if (count == e.getKeywords().size() - 1) {
                    searchKeyWords += " and ";
                } else if (!(count == e.getKeywords().size())) {
                    searchKeyWords += ", ";
                }
            }
            controller.relayFb(String.format(Constants.CMD_SUCCESS_SEARCH_2, searchKeyWords), MsgType.SUCCESS);
        }
    }
}
