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

    @Subscribe
    public void handleListDoneEvent(ListDoneEvent e) {
        for (TaskAttributes item : e.getItems()) {
            System.out.println(item.getDescription());
        }
    }

    @Subscribe
    public void handleListFailEvent(ListFailEvent e) {
        switch (e.getError()) {
            case NON_EXISTENT_TAG:
                System.out.println("Supplied tags do not exist in the database!");
                break;
            case UNKNOWN:
                System.out.println("Unknown error occurred.");
        }
    }

    @Subscribe
    public void handleExitCalledEvent(ExitCalledEvent e) {
        System.out.printf("\nMoriturus te saluto.\n");
    }

    @Subscribe
    public void handleInvalidCommandEvent(InvalidCommandEvent e) {
        System.out.printf("Sorry, I do not understand what you mean by \"%s\" :(\n", e.getInputString());
    }

    @Subscribe
    public void handleAddTaskDoneEvent(AddTaskDoneEvent e) {
        TaskAttributes task = e.getTask();
        System.out.printf("Task added: #%d - %s\n", task.getId(), task.getDescription());
    }

    @Subscribe
    public void handleAddTaskFailEvent(AddTaskFailEvent e) {
        switch (e.getError()) {
            case UNKNOWN:
                System.out.println("Some stupid error occurred.");
        }
    }
}
