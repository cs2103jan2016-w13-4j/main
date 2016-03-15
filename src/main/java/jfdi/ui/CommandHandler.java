package jfdi.ui;

import java.util.ArrayList;

import com.google.common.eventbus.Subscribe;

import edu.emory.mathcs.backport.java.util.Collections;
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

    @Subscribe
    public void handleListDoneEvent(ListDoneEvent e) {

        /*
         * for (TaskAttributes item : e.getItems()) {
         * System.out.println("logic_prior: " + item.getId() +
         * item.getDescription()); } System.out.println(" "); for (ListItem item
         * : controller.importantList) { System.out.println("ui_prior: " +
         * item.getId() + item.getDescription()); }
         */

        controller.importantList.clear();
        int count = 1;
        for (TaskAttributes item : e.getItems()) {
            ListItem listItem = new ListItem(count, item, false);
            controller.importantList.add(listItem);
            controller.indexMapId.add(item.getId());
            count++;
        }
        controller.relayFb(Constants.CMD_SUCCESS_LISTED, MsgType.SUCCESS);
    }

    @Subscribe
    public void handleExitCalledEvent(ExitCalledEvent e) {
        System.out.printf("\nMoriturus te saluto.\n");
    }

    @Subscribe
    public void handleInvalidCommandEvent(InvalidCommandEvent e) {
        controller.relayFb(String.format(Constants.CMD_WARNING_DONTKNOW, e.getInputString()), MsgType.WARNING);
    }

    @Subscribe
    public void handleAddTaskDoneEvent(AddTaskDoneEvent e) {
        TaskAttributes task = e.getTask();
        int index = controller.importantList.size() + 1;
        ListItem listItem = new ListItem(index, task, false);
        controller.importantList.add(listItem);
        controller.indexMapId.add(task.getId());
        controller.relayFb(String.format(Constants.CMD_SUCCESS_ADDED, index, task.getDescription()), MsgType.SUCCESS);
    }

    @Subscribe
    public void handleAddTaskFailEvent(AddTaskFailEvent e) {
        switch (e.getError()) {
            case UNKNOWN:
                controller.relayFb(Constants.CMD_ERROR_CANT_ADD_UNKNOWN, MsgType.ERROR);
                break;
            case EMPTY_DESCRIPTION:
                controller.relayFb(Constants.CMD_ERROR_CANT_ADD_EMPTY, MsgType.ERROR);
                break;
            default:
                break;
        }
    }

    @Subscribe
    public void handleDeleteTaskDoneEvent(DeleteTaskDoneEvent e) {
        ArrayList<Integer> deletedIds = e.getDeletedIds();
        Collections.sort(deletedIds);

        int count = 0;
        for (Integer num : deletedIds) {
            if (controller.indexMapId.get(count) == num) {
                controller.importantList.remove(count);
                controller.indexMapId.remove(count);
                controller.relayFb(String.format(Constants.CMD_SUCCESS_DELETED, count + 1), MsgType.SUCCESS);
            } else {
                count++;
            }
        }

        count = 1;
        for (ListItem item : controller.importantList) {
            if (item.getIndex() == count) {
                count++;
            } else {
                controller.importantList.get(count - 1).setIndex(count);
                count++;
            }
        }
    }

    @Subscribe
    public void handleDeleteTaskFailEvent(DeleteTaskFailEvent e) {
        switch (e.getError()) {
            case UNKNOWN:
                controller.relayFb(Constants.CMD_ERROR_CANT_DELETE, MsgType.ERROR);
                break;
            case NON_EXISTENT_ID:
                //NEED TO CHANGE TO INDEX SOON????
                controller.relayFb(Constants.CMD_ERROR_CANT_DELETE, MsgType.ERROR);
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
                controller.importantList.get(count).setTask(task);
                break;
            }
            count++;
        }
        controller.relayFb(String.format(
                Constants.CMD_SUCCESS_RENAMED, count + 1, task.getDescription()), MsgType.SUCCESS);
    }

    @Subscribe
    public void handleRenameTaskFailEvent(RenameTaskFailEvent e) {
        switch (e.getError()) {
            case UNKNOWN:
                controller.relayFb(Constants.CMD_ERROR_CANT_RENAME_UNKNOWN, MsgType.ERROR);
                break;
            case NON_EXISTENT_ID:
                //NEED TO CHANGE TO INDEX SOON????
                controller.relayFb(String.format(Constants.CMD_ERROR_CANT_RENAME_NO_ID, e.getTaskId()), MsgType.ERROR);
                break;
            case NO_CHANGES:
                controller.relayFb(String.format(
                        Constants.CMD_ERROR_CANT_RENAME_NO_CHANGES, e.getDescription()), MsgType.ERROR);
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
                controller.importantList.get(count).getItem()
                .setStartDateTime(e.getStartDateTime());
                controller.importantList.get(count).getItem()
                .setEndDateTime(e.getEndDateTime());
                break;
            }
            count++;
        }
        controller.relayFb(String.format(Constants.CMD_SUCCESS_RESCHEDULED, count + 1), MsgType.SUCCESS);
    }

    @Subscribe
    public void handleRescheduleTaskFailEvent(RescheduleTaskFailEvent e) {
        switch (e.getError()) {
            case UNKNOWN:
                controller.relayFb(Constants.CMD_ERROR_CANT_RESCHEDULE_UNKNOWN, MsgType.ERROR);
                break;
            case NON_EXISTENT_ID:
                //NEED TO CHANGE TO INDEX SOON????
                controller.relayFb(String.format(
                        Constants.CMD_ERROR_CANT_RESCHEDULE_NO_ID, e.getTaskId()), MsgType.ERROR);
                break;
            case NO_CHANGES:
                controller.relayFb(
                        Constants.CMD_ERROR_CANT_RESCHEDULE_NO_CHANGES + e.getStartDateTime() + " - to - "
                                + e.getEndDateTime() + " -!", MsgType.ERROR);
                break;
            default:
                break;
        }
    }

    public void setController(MainController controller) {
        this.controller = controller;
    }
}
