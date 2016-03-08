package jfdi.ui;

import java.util.ArrayList;

import com.google.common.eventbus.Subscribe;

import jfdi.logic.events.AddTaskDoneEvent;
import jfdi.logic.events.AddTaskFailEvent;
import jfdi.logic.events.DeleteTaskDoneEvent;
import jfdi.logic.events.DeleteTaskFailEvent;
import jfdi.logic.events.ExitCalledEvent;
import jfdi.logic.events.InvalidCommandEvent;
import jfdi.logic.events.ListDoneEvent;
import jfdi.logic.events.ListFailEvent;
import jfdi.logic.events.RenameTaskDoneEvent;
import jfdi.logic.events.RenameTaskFailEvent;
import jfdi.logic.events.RescheduleTaskDoneEvent;
import jfdi.logic.events.RescheduleTaskFailEvent;
import jfdi.storage.data.TaskAttributes;


public class CommandHandler {

    private static final String CMD_ERROR_NONEXIST_TAG = "Supplied tags do not exist in the database!";
    private static final String CMD_ERROR_UNKNOWN = "Unknown error occurred.";
    private static final String CMD_ERROR_CANT_ADD = "Some stupid error occurred. Cannot add task!";
    private static final String CMD_ERROR_CANT_DELETE = "Some stupid error occurred. Cannot delete task!";
    private static final String CMD_ERROR_CANT_RENAME = "Some stupid error occurred. Cannot rename task!";
    private static final String CMD_ERROR_CANT_RESCHEDULE = "Some stupid error occurred. Cannot reschedule task!";
    private static final String CMD_WARNING_DONTKNOW = "Sorry, I do not understand what you mean by \"%s\" :(\n";
    private static final String CMD_SUCCESS_ADDED = "Task #%d - %s added!\n";
    private static final String CMD_SUCCESS_DELETED = "Task #%d deleted!\n";
    private static final String CMD_SUCCESS_RENAMED = "Task #%d renamed to - %s! -\n";
    private static final String CMD_SUCCESS_RESCHEDULED = "Task #%d rescheduled!\n";


    private MainController controller;

    public enum MsgType {
        SUCCESS, WARNING, ERROR, EXIT
    }

    @Subscribe
    public void handleListDoneEvent(ListDoneEvent e) {
        for (TaskAttributes item : e.getItems()) {
            if (!controller.importantList.contains(item)) {
                controller.importantList.add(item);
            }
        }
        controller.displayList();
    }

    @Subscribe
    public void handleListFailEvent(ListFailEvent e) {
        switch (e.getError()) {
            case NON_EXISTENT_TAG:
                controller.relayFb(CMD_ERROR_NONEXIST_TAG, MsgType.ERROR);
                break;
            case UNKNOWN:
                controller.relayFb(CMD_ERROR_UNKNOWN, MsgType.ERROR);
                break;
            default: break;
        }
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
        controller.displayList();
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
        ArrayList<Integer> deletedItems = e.getDeletedIds();
        for (Integer n : deletedItems) {
            controller.importantList.remove(n);
            controller.relayFb(String.format(CMD_SUCCESS_DELETED, n), MsgType.SUCCESS);
        }
        controller.displayList();
    }

    @Subscribe
    public void handleDeleteTaskFailEvent(DeleteTaskFailEvent e) {
        switch (e.getError()) {
            case UNKNOWN:
                controller.relayFb(CMD_ERROR_CANT_DELETE, MsgType.ERROR);
                break;
            default: break;
        }
    }

    @Subscribe
    public void handleRenameTaskDoneEvent(RenameTaskDoneEvent e) {
        TaskAttributes task = e.getTask();
        controller.importantList.set(task.getId(), task);
        controller.relayFb(String.format(CMD_SUCCESS_RENAMED, task.getId(), task.getDescription()), MsgType.SUCCESS);
        controller.displayList();
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
