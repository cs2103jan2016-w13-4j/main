package jfdi.ui;

import java.util.HashSet;

import com.google.common.eventbus.Subscribe;

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
import jfdi.storage.data.TaskAttributes;


public class CommandHandler {

    //private static final String CMD_ERROR_NONEXIST_TAG = "Supplied tags do not exist in the database!";
    //private static final String CMD_ERROR_UNKNOWN = "Unknown error occurred.";
    private static final String CMD_ERROR_CANT_ADD = "Some stupid error occurred. Cannot add task!";
    private static final String CMD_ERROR_CANT_DELETE = "Some stupid error occurred. Cannot delete task!";
    private static final String CMD_ERROR_CANT_RENAME = "Some stupid error occurred. Cannot rename task!";
    private static final String CMD_ERROR_CANT_RESCHEDULE = "Some stupid error occurred. Cannot reschedule task!";
    private static final String CMD_WARNING_DONTKNOW = "Sorry, I do not understand what you mean by \"%s\" :(";
    private static final String CMD_SUCCESS_LISTED = "Here is your requested list! :)";
    private static final String CMD_SUCCESS_ADDED = "Task #%d - %s added! :)";
    private static final String CMD_SUCCESS_DELETED = "Task #%d deleted! :)";
    private static final String CMD_SUCCESS_RENAMED = "Task #%d renamed to - %s -! :)";
    private static final String CMD_SUCCESS_RESCHEDULED = "Task #%d rescheduled! :)";

    private MainController controller;

    public enum MsgType {
        SUCCESS, WARNING, ERROR, EXIT
    }

    @Subscribe
    public void handleListDoneEvent(ListDoneEvent e) {

        /*for (TaskAttributes item : e.getItems()) {
            System.out.println("logic_prior: " + item.getId() + item.getDescription());
        }
        System.out.println(" ");
        for (TaskAttributes item : controller.importantList) {
            System.out.println("ui_prior: " + item.getId() + item.getDescription());
        }*/

        controller.importantList.removeAll(controller.importantList);
        controller.importantList.setAll(e.getItems());

        controller.relayFb(CMD_SUCCESS_LISTED, MsgType.SUCCESS);


    }

    @Subscribe
    public void handleExitCalledEvent(ExitCalledEvent e) {
        System.out.printf("\nMoriturus te saluto.\n");
    }

    @Subscribe
    public void handleInvalidCommandEvent(InvalidCommandEvent e) {
        controller.relayFb(String.format(CMD_WARNING_DONTKNOW, e.getInputString()), MsgType.WARNING);
    }

    @Subscribe
    public void handleAddTaskDoneEvent(AddTaskDoneEvent e) {
        TaskAttributes task = e.getTask();
        controller.importantList.add(task);
        controller.relayFb(String.format(CMD_SUCCESS_ADDED, task.getId(), task.getDescription()), MsgType.SUCCESS);
    }

    @Subscribe
    public void handleAddTaskFailEvent(AddTaskFailEvent e) {
        switch (e.getError()) {
            case UNKNOWN:
                controller.relayFb(CMD_ERROR_CANT_ADD, MsgType.ERROR);
                break;
            default: break;
        }
    }

    @Subscribe
    public void handleDeleteTaskDoneEvent(DeleteTaskDoneEvent e) {
        HashSet<Integer> deletedIds = new HashSet<Integer>(e.getDeletedIds());
        controller.importantList.removeIf(task -> deletedIds.contains(task.getId()));
        for (Integer num : deletedIds) {
            controller.relayFb(String.format(CMD_SUCCESS_DELETED, num), MsgType.SUCCESS);
        }
    }

    @Subscribe
    public void handleDeleteTaskFailEvent(DeleteTaskFailEvent e) {
        switch (e.getError()) {
            case UNKNOWN:
                controller.relayFb(CMD_ERROR_CANT_DELETE, MsgType.ERROR);
                break;
            case NON_EXISTENT_ID:
                controller.relayFb(CMD_ERROR_CANT_DELETE, MsgType.ERROR);
                break;
            default: break;
        }
    }

    @Subscribe
    public void handleRenameTaskDoneEvent(RenameTaskDoneEvent e) {
        TaskAttributes task = e.getTask();
        for (TaskAttributes item : controller.importantList) {
            if (item.getId() == task.getId()) {
                controller.importantList.remove(item);
                controller.importantList.add(task);
            }
        }
        controller.relayFb(String.format(CMD_SUCCESS_RENAMED, task.getId(), task.getDescription()), MsgType.SUCCESS);
    }

    @Subscribe
    public void handleRenameTaskFailEvent(RenameTaskFailEvent e) {
        switch (e.getError()) {
            case UNKNOWN:
                controller.relayFb(CMD_ERROR_CANT_RENAME, MsgType.ERROR);
                break;
            default: break;
        }
    }

    @Subscribe
    public void handleRescheduleTaskDoneEvent(RescheduleTaskDoneEvent e) {
        controller.relayFb(String.format(CMD_SUCCESS_RESCHEDULED, e.getTaskId()), MsgType.SUCCESS);
    }

    @Subscribe
    public void handleRescheduleTaskFailEvent(RescheduleTaskFailEvent e) {
        switch (e.getError()) {
            case UNKNOWN:
                controller.relayFb(CMD_ERROR_CANT_RESCHEDULE, MsgType.ERROR);
                break;
            default: break;
        }
    }

    public void setController(MainController controller) {
        this.controller = controller;
    }
}
