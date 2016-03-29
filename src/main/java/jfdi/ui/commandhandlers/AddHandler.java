package jfdi.ui.commandhandlers;

import com.google.common.eventbus.Subscribe;

import jfdi.logic.events.AddTaskDoneEvent;
import jfdi.logic.events.AddTaskFailedEvent;
import jfdi.storage.apis.TaskAttributes;
import jfdi.ui.Constants;
import jfdi.ui.Constants.ListStatus;
import jfdi.ui.Constants.MsgType;

public class AddHandler extends CommandHandler {
    
    private static AddHandler instance = new AddHandler();
    
    private AddHandler() {
    }
    
    public static AddHandler getInstance() {
        return instance;
    }

    @Subscribe
    public void handleAddTaskDoneEvent(AddTaskDoneEvent e) {
        if (controller.isInternalCall()) {
            // Add any method calls strictly for internal calls here
            return;
        }
        if (!controller.displayStatus.equals(ListStatus.ALL)) {
            switchContext(ListStatus.INCOMPLETE, true);
        }
        TaskAttributes task = e.getTask();
        appendTaskToDisplayList(task, true);
        if (shouldSort()) {
            sortDisplayList();
        }
        controller.relayFb(
            String.format(Constants.CMD_SUCCESS_ADDED, task.getDescription()),
            MsgType.SUCCESS);
        logger.fine(String.format(Constants.LOG_ADDED_SUCCESS, task.getId()));
        controller.updateNotiBubbles();
        controller.listMain.scrollTo(controller.importantList.size() - 1);
    }

    @Subscribe
    public void handleAddTaskFailEvent(AddTaskFailedEvent e) {
        if (controller.isInternalCall()) {
            // Add any method calls strictly for internal calls here
            return;
        }

        switch (e.getError()) {
            case UNKNOWN:
                controller.relayFb(Constants.CMD_ERROR_CANT_ADD_UNKNOWN,
                    MsgType.ERROR);
                logger.fine(String.format(Constants.LOG_ADD_FAIL_UNKNOWN));
                break;
            case EMPTY_DESCRIPTION:
                controller.relayFb(Constants.CMD_ERROR_CANT_ADD_EMPTY,
                    MsgType.ERROR);
                logger.fine(String.format(Constants.LOG_ADD_FAIL_EMPTY));
                break;
            case DUPLICATED_TASK:
                controller.relayFb(Constants.CMD_ERROR_CANT_ADD_DUPLICATE,
                    MsgType.ERROR);
                logger.fine(String.format(Constants.LOG_ADD_FAIL_DUPLICATE));
                break;
            default:
                break;
        }
    }
    
    
}
