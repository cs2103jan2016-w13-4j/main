package jfdi.ui.commandhandlers;

import java.util.ArrayList;

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

        // check size of list of ids > 0
        // check size of id list == task list

        controller.updateNotiBubbles();

        ArrayList<Integer> doneIds = e.getScreenIds();
        Collections.sort(doneIds);

        markTaskOnList(doneIds);
    }

    private void markTaskOnList(ArrayList<Integer> doneIds) {

        if (doneIds.size() == 1) {
            controller.importantList.get(controller.indexMatch.get(doneIds.get(0))).setMarkT();
            controller.importantList.get(controller.indexMatch.get(doneIds.get(0))).strikeOut();
            controller.relayFb(String.format(Constants.CMD_SUCCESS_MARKED_1, doneIds.get(0)), MsgType.SUCCESS);
            controller.listMain.scrollTo(doneIds.get(0));
        } else {
            int count = 0;
            int indexCount = -1;
            String indices = "";
            for (int screenId : doneIds) {
                indexCount = controller.indexMatch.get(screenId);
                indices += "#" + String.valueOf(screenId);
                count++;
                if (count == doneIds.size() - 1) {
                    indices += " and ";
                } else if (!(count == doneIds.size())) {
                    indices += ", ";
                }
                controller.importantList.get(indexCount).setMarkT();
                controller.importantList.get(indexCount).strikeOut();
            }
            controller.listMain.scrollTo(doneIds.get(0));
            controller.relayFb(String.format(Constants.CMD_SUCCESS_MARKED_2, indices), MsgType.SUCCESS);
        }
    }

    @Subscribe
    public void handleMarkTaskFailEvent(MarkTaskFailedEvent e) {

        switch (e.getError()) {
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
