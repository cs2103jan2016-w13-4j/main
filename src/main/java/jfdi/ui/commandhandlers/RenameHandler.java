// @@author A0129538W

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

        TaskAttributes task = e.getTask();

        int displayIndex = findCurrentIndex(task);
        int arrayIndex = controller.indexMatch.get(displayIndex);
        controller.importantList.get(arrayIndex).setDescription(task.getDescription());

        logger.fine(String.format(Constants.LOG_RENAMED_SUCCESS, task.getId()));
        controller.listMain.scrollTo(arrayIndex);
        controller.updateNotiBubbles();
        controller.relayFb(String.format(Constants.CMD_SUCCESS_RENAMED, displayIndex, task.getDescription()),
                MsgType.SUCCESS);
    }

    @Subscribe
    public void handleRenameTaskFailEvent(RenameTaskFailedEvent e) {

        switch (e.getError()) {
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
