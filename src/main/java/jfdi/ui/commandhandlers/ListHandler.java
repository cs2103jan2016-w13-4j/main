package jfdi.ui.commandhandlers;

import com.google.common.eventbus.Subscribe;

import jfdi.logic.events.ListDoneEvent;
import jfdi.ui.Constants;
import jfdi.ui.Constants.ListStatus;
import jfdi.ui.Constants.MsgType;

public class ListHandler extends CommandHandler {

    private static ListHandler instance = new ListHandler();

    private ListHandler() {
    }

    public static ListHandler getInstance() {
        return instance;
    }

    @Subscribe
    public void handleListDoneEvent(ListDoneEvent e) {
        controller.updateBubble(e);
        if (controller.isInternalCall()) {
            // Add any method calls strictly for internal calls here
            return;
        }

        switch (e.getListType()) {
            case ALL:
                controller.switchContext(ListStatus.ALL, false);
                break;
            case COMPLETED:
                controller.switchContext(ListStatus.COMPLETE, false);
                break;
            case INCOMPLETE:
                controller.switchContext(ListStatus.INCOMPLETE, false);
                break;
            case OVERDUE:
                controller.switchContext(ListStatus.OVERDUE, false);
                break;
            case UPCOMING:
                controller.switchContext(ListStatus.UPCOMING, false);
                break;
            default:
                break;

        }
        controller.listTasks(e.getItems(), false);
        controller.relayFb(Constants.CMD_SUCCESS_LISTED, MsgType.SUCCESS);
    }

}
