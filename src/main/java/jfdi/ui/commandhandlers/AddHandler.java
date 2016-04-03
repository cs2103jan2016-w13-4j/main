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

        if (controller.displayStatus.equals(ListStatus.SEARCH)) {
            controller.switchContext(ListStatus.INCOMPLETE, true);
        } else if (!controller.displayStatus.equals(ListStatus.ALL)) {
            controller.switchContext(ListStatus.INCOMPLETE, false);
        }

        TaskAttributes task = e.getTask();
        controller.appendTaskToDisplayList(task, true, false);

        if (controller.shouldSort()) {
            controller.sortDisplayList();
        }

        controller.updateNotiBubbles();

        controller.listMain.scrollTo(controller.importantList.size() - 1);

        controller.relayFb(String.format(Constants.CMD_SUCCESS_ADDED, task.getDescription()), MsgType.SUCCESS);
        logger.fine(String.format(Constants.LOG_ADDED_SUCCESS, task.getId()));
    }

    @Subscribe
    public void handleAddTaskFailEvent(AddTaskFailedEvent e) {

        switch (e.getError()) {
            case UNKNOWN:
                controller.relayFb(Constants.CMD_ERROR_CANT_ADD_UNKNOWN, MsgType.ERROR);
                logger.fine(String.format(Constants.LOG_ADD_FAIL_UNKNOWN));
                break;
            case EMPTY_DESCRIPTION:
                controller.relayFb(Constants.CMD_ERROR_CANT_ADD_EMPTY, MsgType.ERROR);
                logger.fine(String.format(Constants.LOG_ADD_FAIL_EMPTY));
                break;
            case DUPLICATED_TASK:
                controller.relayFb(Constants.CMD_ERROR_CANT_ADD_DUPLICATE, MsgType.ERROR);
                logger.fine(String.format(Constants.LOG_ADD_FAIL_DUPLICATE));
                break;
            default:
                break;
        }
    }

}
