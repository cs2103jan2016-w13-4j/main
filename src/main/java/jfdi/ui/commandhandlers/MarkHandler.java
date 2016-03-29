package jfdi.ui.commandhandlers;

import java.util.ArrayList;
import java.util.Comparator;

import com.google.common.eventbus.Subscribe;

import edu.emory.mathcs.backport.java.util.Collections;
import jfdi.logic.events.MarkTaskDoneEvent;
import jfdi.logic.events.MarkTaskFailedEvent;
import jfdi.ui.Constants;
import jfdi.ui.Constants.MsgType;

public class MarkHandler extends CommandHandler {

    private static MarkHandler instance = new MarkHandler();

    private MarkHandler() {
    }

    public static MarkHandler getInstance() {
        return instance;
    }

    @Subscribe
    public void handleMarkTaskDoneEvent(MarkTaskDoneEvent e) {
        if (controller.isInternalCall()) {
            // Add any method calls strictly for internal calls here
            return;
        }

        ArrayList<Integer> doneIds = e.getScreenIds();
        Collections.sort(doneIds, Comparator.reverseOrder());
        int indexCount = -1;
        for (Integer screenId : doneIds) {
            indexCount = screenId - 1;
            controller.importantList.get(indexCount).setMarkT();
            controller.importantList.get(indexCount).strikeOut();
            refreshDisplay();

            // controller.displayList(controller.displayStatus);
            // logger.fine(String.format(Constants.LOG_DELETED_SUCCESS, num));
        }
        controller.relayFb(String.format(Constants.CMD_SUCCESS_MARKED, indexCount + 1), MsgType.SUCCESS);
        controller.updateNotiBubbles();
        controller.listMain.scrollTo(indexCount);
    }

    @Subscribe
    public void handleMarkTaskFailEvent(MarkTaskFailedEvent e) {
        if (controller.isInternalCall()) {
            // Add any method calls strictly for internal calls here
            return;
        }

        switch (e.getError()) {
            case UNKNOWN:
                controller.relayFb(Constants.CMD_ERROR_CANT_MARK_UNKNOWN, MsgType.ERROR);
                // logger.fine(Constants.LOG_DELETE_FAIL_UNKNOWN);
                break;
            case NON_EXISTENT_ID:
                // NEED TO CHANGE TO INDEX SOON????
                for (Integer screenId : e.getInvalidIds()) {
                    controller.relayFb(String.format(Constants.CMD_ERROR_CANT_MARK_NO_ID, screenId), MsgType.ERROR);
                }
                // logger.fine(Constants.LOG_DELETE_FAIL_NOID);
                break;
            default:
                break;
        }
    }
}
