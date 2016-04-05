package jfdi.ui.commandhandlers;

import java.util.ArrayList;

import com.google.common.eventbus.Subscribe;

import edu.emory.mathcs.backport.java.util.Collections;
import jfdi.logic.events.DeleteTaskDoneEvent;
import jfdi.logic.events.DeleteTaskFailedEvent;
import jfdi.ui.Constants;
import jfdi.ui.Constants.MsgType;
import jfdi.ui.items.ListItem;

public class DeleteHandler extends CommandHandler {

    private static DeleteHandler instance = new DeleteHandler();

    private DeleteHandler() {
    }

    public static DeleteHandler getInstance() {
        return instance;
    }

    @Subscribe
    public void handleDeleteTaskDoneEvent(DeleteTaskDoneEvent e) {

        // check size of list of ids > 0
        // check size of id list == task list

        ArrayList<Integer> deletedIds = e.getDeletedIds();
        Collections.sort(deletedIds);

        removeTaskFromList(deletedIds);
        reorderListIndices();

        controller.transListCmd();

        controller.updateNotiBubbles();
    }

    @Subscribe
    public void handleDeleteTaskFailEvent(DeleteTaskFailedEvent e) {

        // check size of list of ids > 0
        // check size of id list == task list

        switch (e.getError()) {
            case UNKNOWN:
                controller.relayFb(Constants.CMD_ERROR_CANT_DELETE_UNKNOWN, MsgType.ERROR);
                logger.fine(Constants.LOG_DELETE_FAIL_UNKNOWN);
                break;
            case NON_EXISTENT_ID:
                for (Integer screenId : e.getInvalidIds()) {
                    controller.relayFb(String.format(Constants.CMD_ERROR_CANT_DELETE_NO_ID, screenId), MsgType.ERROR);
                }
                logger.fine(Constants.LOG_DELETE_FAIL_NOID);
                break;
            default:
                break;
        }
    }

    private void removeTaskFromList(ArrayList<Integer> deletedIds) {

        if (deletedIds.size() == 1) {
            controller.importantList.remove(controller.indexMatch.get(deletedIds.get(0)));
            controller.relayFb(String.format(Constants.CMD_SUCCESS_DELETED_1, deletedIds.get(0)), MsgType.SUCCESS);
        } else {
            int count = 0;
            int indexCount;
            String indices = "";
            Collections.sort(deletedIds, Collections.reverseOrder());
            for (int screenId : deletedIds) {
                indexCount = controller.indexMatch.get(screenId);
                indices += "#" + String.valueOf(screenId);
                count++;
                if (count == deletedIds.size() - 1) {
                    indices += " and ";
                } else if (!(count == deletedIds.size())) {
                    indices += ", ";
                }
                controller.importantList.remove(indexCount);
            }
            controller.relayFb(String.format(Constants.CMD_SUCCESS_DELETED_2, indices), MsgType.SUCCESS);
        }
    }

    private void reorderListIndices() {

        int indexCount = 1;
        for (ListItem item : controller.importantList) {
            if (item.getIsHeader()) {
                continue;
            }

            if (item.getIndex() != indexCount) {
                item.setIndex(indexCount);
            }
            indexCount++;
        }
    }
}
