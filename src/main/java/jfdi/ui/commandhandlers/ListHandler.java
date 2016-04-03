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
        
        switch (e.getListType()) {
            case ALL:
                controller.switchContext(ListStatus.ALL, true);
                break;
            case COMPLETED:
                controller.switchContext(ListStatus.COMPLETE, true);
                break;
            case INCOMPLETE:
                controller.switchContext(ListStatus.INCOMPLETE, true);
                break;
            case OVERDUE:
                controller.switchContext(ListStatus.OVERDUE, true);
                break;
            case UPCOMING:
                controller.switchContext(ListStatus.UPCOMING, true);
                break;
            default:
                break;

        }

        controller.updateNotiBubbles();
        controller.relayFb(String.format(Constants.CMD_SUCCESS_LISTED, e.getListType()), MsgType.SUCCESS);
    }

}
