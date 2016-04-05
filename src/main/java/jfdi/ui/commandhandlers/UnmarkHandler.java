package jfdi.ui.commandhandlers;

import java.util.ArrayList;

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

        // check size of list of ids > 0
        // check size of id list == task list

        controller.updateNotiBubbles();

        ArrayList<Integer> undoneIds = e.getScreenIds();
        Collections.sort(undoneIds);

        unmarkTaskOnList(undoneIds);
    }

    private void unmarkTaskOnList(ArrayList<Integer> undoneIds) {

        if (undoneIds.size() == 1) {
            controller.importantList.get(controller.indexMatch.get(undoneIds.get(0))).setMarkF();
            controller.importantList.get(controller.indexMatch.get(undoneIds.get(0))).removeStrike();
            controller.relayFb(String.format(Constants.CMD_SUCCESS_UNMARKED_1, undoneIds.get(0)), MsgType.SUCCESS);
            controller.listMain.scrollTo(undoneIds.get(0));
        } else {
            int count = 0;
            int indexCount = -1;
            String indices = "";
            for (int screenId : undoneIds) {
                indexCount = controller.indexMatch.get(screenId);
                indices += "#" + String.valueOf(screenId);
                count++;
                if (count == undoneIds.size() - 1) {
                    indices += " and ";
                } else if (!(count == undoneIds.size())) {
                    indices += ", ";
                }
                controller.importantList.get(indexCount).setMarkF();
                controller.importantList.get(indexCount).removeStrike();
            }
            controller.listMain.scrollTo(undoneIds.get(0));
            controller.relayFb(String.format(Constants.CMD_SUCCESS_UNMARKED_2, indices), MsgType.SUCCESS);
        }
    }

    @Subscribe
    public void handleUnmarkTaskFailEvent(UnmarkTaskFailedEvent e) {

        switch (e.getError()) {
            case NON_EXISTENT_ID:
                // NEED TO CHANGE TO INDEX SOON????
                for (Integer screenId : e.getInvalidIds()) {
                    controller.relayFb(String.format(Constants.CMD_ERROR_CANT_UNMARK_NO_ID, screenId), MsgType.ERROR);
                }
                // logger.fine(Constants.LOG_DELETE_FAIL_NOID);
                break;
            default:
                break;
        }
    }
}
