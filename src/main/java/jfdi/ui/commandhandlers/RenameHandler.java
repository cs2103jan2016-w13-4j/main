package jfdi.ui.commandhandlers;

import com.google.common.eventbus.Subscribe;

import jfdi.logic.events.RenameTaskDoneEvent;
import jfdi.logic.events.RenameTaskFailedEvent;
import jfdi.storage.apis.TaskAttributes;
import jfdi.ui.Constants;
import jfdi.ui.Constants.MsgType;

public class RenameHandler extends CommandHandler {

    private static RenameHandler instance = new RenameHandler();

    private RenameHandler() {
    }

    public static RenameHandler getInstance() {
        return instance;
    }

    @Subscribe
    public void handleRenameTaskDoneEvent(RenameTaskDoneEvent e) {
        if (controller.isInternalCall()) {
            // Add any method calls strictly for internal calls here
            return;
        }

        TaskAttributes task = e.getTask();
        int count = 0;
        for (int i = 0; i < controller.importantList.size(); i++) {
            if (controller.getIdFromIndex(i) == task.getId()) {
                controller.importantList.get(i).setDescription(task.getDescription());
                count = i;
                break;
            }
        }
        controller.relayFb(String.format(Constants.CMD_SUCCESS_RENAMED, count + 1, task.getDescription()),
                MsgType.SUCCESS);
        logger.fine(String.format(Constants.LOG_RENAMED_SUCCESS, task.getId()));
        controller.updateNotiBubbles();
        controller.listMain.scrollTo(count);
    }

    @Subscribe
    public void handleRenameTaskFailEvent(RenameTaskFailedEvent e) {
        if (controller.isInternalCall()) {
            // Add any method calls strictly for internal calls here
            return;
        }

        switch (e.getError()) {
            case UNKNOWN:
                controller.relayFb(Constants.CMD_ERROR_CANT_RENAME_UNKNOWN, MsgType.ERROR);
                logger.fine(Constants.LOG_RENAME_FAIL_UNKNOWN);
                break;
            case NON_EXISTENT_ID:
                // NEED TO CHANGE TO INDEX SOON????
                controller.relayFb(String.format(Constants.CMD_ERROR_CANT_RENAME_NO_ID, e.getScreenId()),
                        MsgType.ERROR);
                logger.fine(Constants.LOG_RENAME_FAIL_NOID);
                break;
            case NO_CHANGES:
                controller.relayFb(String.format(Constants.CMD_ERROR_CANT_RENAME_NO_CHANGES, e.getDescription()),
                        MsgType.ERROR);
                logger.fine(Constants.LOG_RENAME_FAIL_NOCHANGE);
                break;
            case DUPLICATED_TASK:
                controller.relayFb(Constants.CMD_ERROR_CANT_RENAME_DUPLICATE, MsgType.ERROR);
                logger.fine(String.format(Constants.LOG_RENAME_FAIL_DUPLICATE));
                break;
            default:
                break;
        }
    }
}
