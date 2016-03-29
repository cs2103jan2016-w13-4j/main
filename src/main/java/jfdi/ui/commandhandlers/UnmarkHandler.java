package jfdi.ui.commandhandlers;

import java.util.ArrayList;
import java.util.Comparator;

import com.google.common.eventbus.Subscribe;

import edu.emory.mathcs.backport.java.util.Collections;
import jfdi.logic.events.UnmarkTaskDoneEvent;
import jfdi.logic.events.UnmarkTaskFailedEvent;
import jfdi.ui.Constants;
import jfdi.ui.Constants.MsgType;

public class UnmarkHandler extends CommandHandler {

    private static UnmarkHandler instance = new UnmarkHandler();

    private UnmarkHandler() {
    }

    public static UnmarkHandler getInstance() {
        return instance;
    }

    @Subscribe
    public void handleUnmarkTaskDoneEvent(UnmarkTaskDoneEvent e) {
        if (controller.isInternalCall()) {
            // Add any method calls strictly for internal calls here
            return;
        }

        ArrayList<Integer> undoneIds = e.getScreenIds();
        Collections.sort(undoneIds, Comparator.reverseOrder());
        int indexCount = -1;
        for (Integer screenId : undoneIds) {
            indexCount = screenId - 1;
            controller.importantList.get(indexCount).setMarkF();
            controller.importantList.get(indexCount).removeStrike();
            refreshDisplay();
            // controller.displayList(controller.displayStatus);
            // logger.fine(String.format(Constants.LOG_DELETED_SUCCESS, num));
        }
        controller.relayFb(
            String.format(Constants.CMD_SUCCESS_UNMARKED, indexCount + 1),
            MsgType.SUCCESS);
        controller.updateNotiBubbles();
        controller.listMain.scrollTo(indexCount);
    }

    @Subscribe
    public void handleUnmarkTaskFailEvent(UnmarkTaskFailedEvent e) {
        if (controller.isInternalCall()) {
            // Add any method calls strictly for internal calls here
            return;
        }

        switch (e.getError()) {
            case UNKNOWN:
                controller.relayFb(Constants.CMD_ERROR_CANT_UNMARK_UNKNOWN,
                    MsgType.ERROR);
                // logger.fine(Constants.LOG_DELETE_FAIL_UNKNOWN);
                break;
            case NON_EXISTENT_ID:
                // NEED TO CHANGE TO INDEX SOON????
                for (Integer screenId : e.getInvalidIds()) {
                    controller.relayFb(String.format(
                        Constants.CMD_ERROR_CANT_UNMARK_NO_ID, screenId),
                        MsgType.ERROR);
                }
                // logger.fine(Constants.LOG_DELETE_FAIL_NOID);
                break;
            default:
                break;
        }
    }
}
