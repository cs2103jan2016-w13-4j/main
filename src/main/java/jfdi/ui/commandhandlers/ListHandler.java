package jfdi.ui.commandhandlers;

import com.google.common.eventbus.Subscribe;

import jfdi.logic.events.ListDoneEvent;
import jfdi.ui.Constants;
import jfdi.ui.Constants.ListStatus;
import jfdi.ui.Constants.MsgType;

public class ListHandler extends CommandHandler {
    
    @Subscribe
    public void handleListDoneEvent(ListDoneEvent e) {
        updateBubble(e);
        if (controller.isInternalCall()) {
            // Add any method calls strictly for internal calls here
            return;
        }

        switch (e.getListType()) {
            case ALL:
                switchContext(ListStatus.ALL, false);
                break;
            case COMPLETED:
                switchContext(ListStatus.COMPLETE, false);
                break;
            case INCOMPLETE:
                switchContext(ListStatus.INCOMPLETE, false);
                break;
            case OVERDUE:
                switchContext(ListStatus.OVERDUE, false);
                break;
            case UPCOMING:
                switchContext(ListStatus.UPCOMING, false);
                break;
            default:
                break;

        }
        listTasks(e.getItems(), false);
        controller.relayFb(Constants.CMD_SUCCESS_LISTED, MsgType.SUCCESS);
    }

}
