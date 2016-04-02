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
                controller.switchContext(ListStatus.ALL, false);
                break;
            case COMPLETED:
                controller.switchContext(ListStatus.COMPLETE, false);
                break;
            case INCOMPLETE:
                controller.switchContext(ListStatus.INCOMPLETE, false);
                //divide list into different parts
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

        controller.updateNotiBubbles();
        controller.listTasks(e.getItems(), false);
        controller.relayFb(String.format(Constants.CMD_SUCCESS_LISTED, e.getListType()), MsgType.SUCCESS);
    }

}
