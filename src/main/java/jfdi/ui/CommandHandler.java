package jfdi.ui;

import java.util.ArrayList;

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

import com.google.common.eventbus.Subscribe;

import edu.emory.mathcs.backport.java.util.Collections;

public class CommandHandler {

    private static final String CMD_ERROR_CANT_ADD_UNKNOWN = "Some stupid error occurred. Cannot add task!";
    private static final String CMD_ERROR_CANT_ADD_EMPTY = "Cannot add an empty task!";
    private static final String CMD_ERROR_CANT_DELETE = "Some stupid error occurred. Cannot delete task!";
    private static final String CMD_ERROR_CANT_RENAME_UNKNOWN = "Some stupid error occurred. Cannot rename task!";
    private static final String CMD_ERROR_CANT_RENAME_NO_ID = "Cannot rename task. The ID #%d does not exist!";
    private static final String CMD_ERROR_CANT_RENAME_NO_CHANGES = "No difference between new and old name - %s -!";
    private static final String CMD_ERROR_CANT_RESCHEDULE_UNKNOWN = "Some error occurred. Cannot reschedule task!";
    private static final String CMD_ERROR_CANT_RESCHEDULE_NO_ID = "Cannot reschedule task. The ID #%d does not exist!";
    private static final String CMD_ERROR_CANT_RESCHEDULE_NO_CHANGES = "No difference between new and old schedule - ";

    private static final String CMD_SUCCESS_LISTED = "Here is your requested list! :)";
    private static final String CMD_SUCCESS_ADDED = "Task #%d - %s added! :)";
    private static final String CMD_SUCCESS_DELETED = "Task #%d deleted! :)";
    private static final String CMD_SUCCESS_RENAMED = "Task #%d renamed to - %s -! :)";
    private static final String CMD_SUCCESS_RESCHEDULED = "Task #%d rescheduled! :)";

    private static final String CMD_WARNING_DONTKNOW = "Sorry, I do not understand what you mean by \"%s\" :(";

    private MainController controller;

    public enum MsgType {
        SUCCESS,
        WARNING,
        ERROR,
        EXIT
    }

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
        controller.relayFb(CMD_SUCCESS_LISTED, MsgType.SUCCESS);
    }

    @Subscribe
    public void handleExitCalledEvent(ExitCalledEvent e) {
        System.out.printf("\nMoriturus te saluto.\n");
    }

    @Subscribe
    public void handleInvalidCommandEvent(InvalidCommandEvent e) {
        controller.relayFb(
                String.format(CMD_WARNING_DONTKNOW, e.getInputString()),
                MsgType.WARNING);
    }

    @Subscribe
    public void handleAddTaskDoneEvent(AddTaskDoneEvent e) {
        TaskAttributes task = e.getTask();
        int index = controller.importantList.size() + 1;
        ListItem listItem = new ListItem(index, task, false);
        controller.importantList.add(listItem);
        controller.indexMapId.add(task.getId());
        controller.relayFb(
                String.format(CMD_SUCCESS_ADDED, index, task.getDescription()),
                MsgType.SUCCESS);
    }

    @Subscribe
    public void handleAddTaskFailEvent(AddTaskFailEvent e) {
        switch (e.getError()) {
            case UNKNOWN:
                controller.relayFb(CMD_ERROR_CANT_ADD_UNKNOWN, MsgType.ERROR);
                break;
            case EMPTY_DESCRIPTION:
                controller.relayFb(CMD_ERROR_CANT_ADD_EMPTY, MsgType.ERROR);
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
                controller.relayFb(
                        String.format(CMD_SUCCESS_DELETED, count + 1),
                        MsgType.SUCCESS);
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

        // controller.importantList.removeIf(listItem ->
        // deletedIds.contains(task.getId()));
        /*
         * int upperBd = controller.importantList.size(); for (Integer num :
         * deletedIds) { int count = 0; while (count < upperBd) { if
         * (controller.indexMapId.get(count) == num) { for (int i = count; i <
         * upperBd; i++) { }
         * controller.relayFb(String.format(CMD_SUCCESS_DELETED, count+1),
         * MsgType.SUCCESS); } else { count++; } } }
         */
    }

    @Subscribe
    public void handleDeleteTaskFailEvent(DeleteTaskFailEvent e) {
        switch (e.getError()) {
            case UNKNOWN:
                controller.relayFb(CMD_ERROR_CANT_DELETE, MsgType.ERROR);
                break;
            case NON_EXISTENT_ID:
                // NEED TO CHANGE TO INDEX SOON????
                controller.relayFb(CMD_ERROR_CANT_DELETE, MsgType.ERROR);
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
        controller.relayFb(
                String.format(CMD_SUCCESS_RENAMED, count + 1,
                        task.getDescription()), MsgType.SUCCESS);
    }

    @Subscribe
    public void handleRenameTaskFailEvent(RenameTaskFailEvent e) {
        switch (e.getError()) {
            case UNKNOWN:
                controller
                        .relayFb(CMD_ERROR_CANT_RENAME_UNKNOWN, MsgType.ERROR);
                break;
            case NON_EXISTENT_ID:
                // NEED TO CHANGE TO INDEX SOON????
                controller.relayFb(
                        String.format(CMD_ERROR_CANT_RENAME_NO_ID,
                                e.getTaskId()), MsgType.ERROR);
                break;
            case NO_CHANGES:
                controller.relayFb(
                        String.format(CMD_ERROR_CANT_RENAME_NO_CHANGES,
                                e.getDescription()), MsgType.ERROR);
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
        controller.relayFb(String.format(CMD_SUCCESS_RESCHEDULED, count + 1),
                MsgType.SUCCESS);
    }

    @Subscribe
    public void handleRescheduleTaskFailEvent(RescheduleTaskFailEvent e) {
        switch (e.getError()) {
            case UNKNOWN:
                controller.relayFb(CMD_ERROR_CANT_RESCHEDULE_UNKNOWN,
                        MsgType.ERROR);
                break;
            case NON_EXISTENT_ID:
                // NEED TO CHANGE TO INDEX SOON????
                controller.relayFb(
                        String.format(CMD_ERROR_CANT_RESCHEDULE_NO_ID,
                                e.getTaskId()), MsgType.ERROR);
                break;
            case NO_CHANGES:
                controller.relayFb(
                        CMD_ERROR_CANT_RESCHEDULE_NO_CHANGES
                                + e.getStartDateTime() + " - to - "
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
