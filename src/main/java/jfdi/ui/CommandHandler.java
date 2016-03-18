package jfdi.ui;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import edu.emory.mathcs.backport.java.util.Collections;
import jfdi.common.utilities.JfdiLogger;
import jfdi.logic.events.AddTaskDoneEvent;
import jfdi.logic.events.AddTaskFailEvent;
import jfdi.logic.events.DeleteTaskDoneEvent;
import jfdi.logic.events.DeleteTaskFailEvent;
import jfdi.logic.events.ExitCalledEvent;
import jfdi.logic.events.InvalidCommandEvent;
import jfdi.logic.events.ListDoneEvent;
import jfdi.logic.events.RenameTaskDoneEvent;
import jfdi.logic.events.RenameTaskFailEvent;
import jfdi.logic.events.RescheduleTaskDoneEvent;
import jfdi.logic.events.RescheduleTaskFailEvent;
import jfdi.storage.apis.TaskAttributes;
import jfdi.ui.Constants.MsgType;

public class CommandHandler {

    private MainController controller;
    private Logger logger = JfdiLogger.getLogger();

    @Subscribe
    public void handleListDoneEvent(ListDoneEvent e) {

        logger.fine(Constants.LOG_LOGIC_LIST);
        for (TaskAttributes item : e.getItems()) {
            logger.fine("#" + item.getId() + "  " + item.getDescription());
        }

        controller.importantList.clear();
        controller.indexMapId.clear();

        int count = 1;
        for (TaskAttributes item : e.getItems()) {
            ListItem listItem = new ListItem(count, item, false);
            controller.importantList.add(listItem);
            controller.indexMapId.add(item.getId());
            count++;
        }
        controller.relayFb(Constants.CMD_SUCCESS_LISTED, MsgType.SUCCESS);

        logger.fine(Constants.LOG_SUCCESS_LISTED);
        logger.fine(Constants.LOG_UI_LIST);
        for (ListItem item : controller.importantList) {
            logger.fine(item.toString());
        }
        count = 1;
        logger.fine(Constants.LOG_LOGIC_LIST);
        for (Integer num : controller.indexMapId) {
            logger.fine("Index " + count + " => ID" + num);
            count++;
        }


    }

    @Subscribe
    public void handleExitCalledEvent(ExitCalledEvent e) {
        System.out.printf("\nMoriturus te saluto.\n");
        System.exit(0);
        logger.fine(Constants.LOG_USER_EXIT);
    }

    @Subscribe
    public void handleInvalidCommandEvent(InvalidCommandEvent e) {
        controller.relayFb(String.format(Constants.CMD_WARNING_DONTKNOW, e.getInputString()), MsgType.WARNING);
        logger.fine(Constants.LOG_INVALID_COMMAND);
    }

    @Subscribe
    public void handleAddTaskDoneEvent(AddTaskDoneEvent e) {
        TaskAttributes task = e.getTask();
        int index = controller.importantList.size() + 1;
        ListItem listItem = new ListItem(index, task, false);
        controller.importantList.add(listItem);
        controller.indexMapId.add(task.getId());
        controller.relayFb(String.format(Constants.CMD_SUCCESS_ADDED, index, task.getDescription()), MsgType.SUCCESS);
        logger.fine(String.format(Constants.LOG_ADDED_SUCCESS, task.getId()));
    }

    @Subscribe
    public void handleAddTaskFailEvent(AddTaskFailEvent e) {
        switch (e.getError()) {
            case UNKNOWN:
                controller.relayFb(Constants.CMD_ERROR_CANT_ADD_UNKNOWN, MsgType.ERROR);
                logger.fine(String.format(Constants.LOG_ADD_FAIL_UNKNOWN));
                break;
            case EMPTY_DESCRIPTION:
                controller.relayFb(Constants.CMD_ERROR_CANT_ADD_EMPTY, MsgType.ERROR);
                logger.fine(String.format(Constants.LOG_ADD_FAIL_EMPTY));
                break;
            default:
                break;
        }
    }

    @Subscribe
    public void handleDeleteTaskDoneEvent(DeleteTaskDoneEvent e) {
        ArrayList<Integer> deletedIds = e.getDeletedIds();
        Collections.sort(deletedIds);

        int indexCount = 0;
        for (Integer num : deletedIds) {
            if (controller.indexMapId.get(indexCount) == num) {
                controller.importantList.remove(indexCount);
                controller.indexMapId.remove(indexCount);
                controller.relayFb(String.format(Constants.CMD_SUCCESS_DELETED, indexCount + 1), MsgType.SUCCESS);
                logger.fine(String.format(Constants.LOG_DELETED_SUCCESS, num));
            } else {
                indexCount++;
            }
        }

        /*indexCount = 1;
        logger.fine(Constants.LOG_UI_LIST);
        for (ListItem item : controller.importantList) {
            if (item.getIndex() == indexCount) {
                indexCount++;
            } else {
                controller.importantList.get(indexCount - 1).setIndex(indexCount);
                indexCount++;
            }
            logger.fine(item.toString());
        }*/

        indexCount = 1;
        logger.fine(Constants.LOG_LOGIC_LIST);
        for (Integer num : controller.indexMapId) {
            logger.fine("Index " + indexCount + " => ID" + num);
            indexCount++;
        }
    }

    @Subscribe
    public void handleDeleteTaskFailEvent(DeleteTaskFailEvent e) {
        switch (e.getError()) {
            case UNKNOWN:
                controller.relayFb(Constants.CMD_ERROR_CANT_DELETE, MsgType.ERROR);
                logger.fine(Constants.LOG_DELETE_FAIL_UNKNOWN);
                break;
            case NON_EXISTENT_ID:
                //NEED TO CHANGE TO INDEX SOON????
                controller.relayFb(Constants.CMD_ERROR_CANT_DELETE, MsgType.ERROR);
                logger.fine(Constants.LOG_DELETE_FAIL_NOID);
                break;
            default:
                break;
        }
    }

    @Subscribe
    public void handleRenameTaskDoneEvent(RenameTaskDoneEvent e) {
        TaskAttributes task = e.getTask();
        int count = 0;
        for (Integer id : controller.indexMapId) {
            if (id == task.getId()) {
                controller.importantList.get(count).setDescription(task.getDescription());
                break;
            }
            count++;
        }
        controller.relayFb(String.format(
                Constants.CMD_SUCCESS_RENAMED, count + 1, task.getDescription()), MsgType.SUCCESS);
        logger.fine(String.format(Constants.LOG_RENAMED_SUCCESS, task.getId()));
    }

    @Subscribe
    public void handleRenameTaskFailEvent(RenameTaskFailEvent e) {
        switch (e.getError()) {
            case UNKNOWN:
                controller.relayFb(Constants.CMD_ERROR_CANT_RENAME_UNKNOWN, MsgType.ERROR);
                logger.fine(Constants.LOG_RENAME_FAIL_UNKNOWN);
                break;
            case NON_EXISTENT_ID:
                //NEED TO CHANGE TO INDEX SOON????
                controller.relayFb(String.format(Constants.CMD_ERROR_CANT_RENAME_NO_ID, e.getTaskId()), MsgType.ERROR);
                logger.fine(Constants.LOG_RENAME_FAIL_NOID);
                break;
            case NO_CHANGES:
                controller.relayFb(String.format(
                        Constants.CMD_ERROR_CANT_RENAME_NO_CHANGES, e.getDescription()), MsgType.ERROR);
                logger.fine(Constants.LOG_RENAME_FAIL_NOCHANGE);
                break;
            default:
                break;
        }
    }

    @Subscribe
    public void handleRescheduleTaskDoneEvent(RescheduleTaskDoneEvent e) {
        int count = 0;
        for (Integer id : controller.indexMapId) {
            if (id == e.getTaskId()) {
                controller.importantList.get(count).setTimeDate(e.getStartDateTime(), e.getEndDateTime());
                break;
            }
            count++;
        }
        controller.relayFb(String.format(Constants.CMD_SUCCESS_RESCHEDULED, count + 1), MsgType.SUCCESS);
        logger.fine(String.format(Constants.LOG_RESCHED_SUCCESS, e.getTaskId()));
    }

    @Subscribe
    public void handleRescheduleTaskFailEvent(RescheduleTaskFailEvent e) {
        switch (e.getError()) {
            case UNKNOWN:
                controller.relayFb(Constants.CMD_ERROR_CANT_RESCHEDULE_UNKNOWN, MsgType.ERROR);
                logger.fine(Constants.LOG_RESCHE_FAIL_UNKNOWN);
                break;
            case NON_EXISTENT_ID:
                //NEED TO CHANGE TO INDEX SOON????
                controller.relayFb(String.format(
                        Constants.CMD_ERROR_CANT_RESCHEDULE_NO_ID, e.getTaskId()), MsgType.ERROR);
                logger.fine(Constants.LOG_RESCHE_FAIL_NOID);
                break;
            case NO_CHANGES:
                controller.relayFb(
                        Constants.CMD_ERROR_CANT_RESCHEDULE_NO_CHANGES + e.getStartDateTime() + " - to - "
                                + e.getEndDateTime() + " -!", MsgType.ERROR);
                logger.fine(Constants.LOG_RESCHE_FAIL_NOCHANGE);
                break;
            default:
                break;
        }
    }

    public void setController(MainController controller) {
        this.controller = controller;
    }
}
