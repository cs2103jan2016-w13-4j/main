package jfdi.ui.commandhandlers;

import java.util.ArrayList;
import java.util.Comparator;

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
        if (controller.isInternalCall()) {
            // Add any method calls strictly for internal calls here
            return;
        }

        ArrayList<Integer> deletedIds = e.getDeletedIds();
        Collections.sort(deletedIds, Comparator.reverseOrder());

        int indexCount = -1;
        for (int screenId : deletedIds) {
            indexCount = screenId - 1;
            controller.importantList.remove(indexCount);
            logger.fine(String.format(Constants.LOG_DELETED_SUCCESS, screenId));
        }
        controller.relayFb(Constants.CMD_SUCCESS_DELETED, MsgType.SUCCESS);

        indexCount = 1;
        for (ListItem item : controller.importantList) {
            if (item.getIndex() != indexCount) {
                item.setIndex(indexCount);
            }
            indexCount++;
        }
        controller.updateNotiBubbles();
    }

    @Subscribe
    public void handleDeleteTaskFailEvent(DeleteTaskFailedEvent e) {
        if (controller.isInternalCall()) {
            // Add any method calls strictly for internal calls here
            return;
        }

        switch (e.getError()) {
            case UNKNOWN:
                controller.relayFb(Constants.CMD_ERROR_CANT_DELETE_UNKNOWN,
                    MsgType.ERROR);
                logger.fine(Constants.LOG_DELETE_FAIL_UNKNOWN);
                break;
            case NON_EXISTENT_ID:
                for (Integer screenId : e.getInvalidIds()) {
                    controller.relayFb(String.format(
                        Constants.CMD_ERROR_CANT_DELETE_NO_ID, screenId),
                        MsgType.ERROR);
                }
                logger.fine(Constants.LOG_DELETE_FAIL_NOID);
                break;
            default:
                break;
        }
    }

}
