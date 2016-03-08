package jfdi.ui;

import com.google.common.eventbus.Subscribe;

import jfdi.logic.events.AddTaskDoneEvent;
import jfdi.logic.events.AddTaskFailEvent;
import jfdi.logic.events.ExitCalledEvent;
import jfdi.logic.events.InvalidCommandEvent;
import jfdi.logic.events.ListDoneEvent;
import jfdi.logic.events.ListFailEvent;
import jfdi.storage.data.TaskAttributes;


public class CommandHandler {

    private static final String CMD_ERROR_NONEXIST_TAG = "Supplied tags do not exist in the database!";
    private static final String CMD_ERROR_UNKNOWN = "Unknown error occurred.";
    private static final String CMD_ERROR_CANT_ADD = "Some stupid error occurred.";
    private static final String CMD_WARNING_DONTKNOW = "Sorry, I do not understand what you mean by \"%s\" :(\n";
    private static final String CMD_SUCCESS_ADDED = "Task added: #%d - %s\n";

    private MainController controller;

    public enum MsgType {
        SUCCESS, WARNING, ERROR, EXIT
    }

    @Subscribe
    public void handleListDoneEvent(ListDoneEvent e) {
        for (TaskAttributes item : e.getItems()) {

            System.out.println(item.getDescription());
            System.out.println(item.getId());
        }
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

    public void setController(MainController controller) {
        this.controller = controller;
    }
}
