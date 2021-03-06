// @@author A0129538W

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

        TaskAttributes task = e.getTask();

        switch (controller.displayStatus) {
            case INCOMPLETE:
                controller.switchContext(ListStatus.INCOMPLETE, true);
                break;
            case OVERDUE:
                if (task.isOverdue()) {
                    controller.switchContext(ListStatus.OVERDUE, true);
                } else {
                    controller.switchContext(ListStatus.INCOMPLETE, true);
                }
                break;
            case UPCOMING:
                if (task.isUpcoming()) {
                    controller.switchContext(ListStatus.UPCOMING, true);
                } else {
                    controller.switchContext(ListStatus.INCOMPLETE, true);
                }
                break;
            case ALL:
                controller.appendTaskToDisplayList(task, true);
                break;
            case COMPLETE:
                controller.switchContext(ListStatus.INCOMPLETE, true);
                break;
            case SURPRISE:
                controller.switchContext(ListStatus.INCOMPLETE, true);
                break;
            case SEARCH:
                controller.switchContext(ListStatus.INCOMPLETE, true);
                break;
            default:
                break;
        }

        controller.updateNotiBubbles();
        int index = findCurrentIndex(task);
        controller.listMain.scrollTo(index); // not working properly for
                                             // overdue, upcoming and search
                                             // when index > 5
        logger.fine(String.format(Constants.LOG_ADDED_SUCCESS, task.getId()));
        controller.relayFb(String.format(Constants.CMD_SUCCESS_ADDED, index, task.getDescription()), MsgType.SUCCESS);
    }

    @Subscribe
    public void handleAddTaskFailEvent(AddTaskFailedEvent e) {

        switch (e.getError()) {
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
