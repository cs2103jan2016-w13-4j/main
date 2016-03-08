package dummy;

import java.util.stream.Collectors;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import jfdi.logic.ControlCenter;
import jfdi.logic.events.AddTaskDoneEvent;
import jfdi.logic.events.AddTaskFailEvent;
import jfdi.logic.events.DeleteTaskDoneEvent;
import jfdi.logic.events.DeleteTaskFailEvent;
import jfdi.logic.events.ExitCalledEvent;
import jfdi.logic.events.InvalidCommandEvent;
import jfdi.logic.events.ListDoneEvent;
import jfdi.storage.data.TaskAttributes;
import jfdi.ui.UI;

/**
 * Example of using EventBus and Commands
 *
 * @author Liu Xinan
 */
public class DummyUI {

    private static EventBus eventBus = UI.getEventBus();

    //=============================================================
    // You can put all event handlers in a single class,
    // or put them in multiple classes, according to command types.
    //=============================================================

    @Subscribe
    public void handleListDoneEvent(ListDoneEvent e) {
        for (TaskAttributes item : e.getItems()) {
            System.out.println(item.getDescription());
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
                break;
            default: break;
        }
    }

    @Subscribe
    public void handleDeleteTaskDoneEvent(DeleteTaskDoneEvent e) {
        String list = e.getDeletedIds().stream()
            .map(Object::toString)
            .collect(Collectors.joining(", "));
        System.out.printf("Task%s deleted: %s\n", list.length() == 1 ? "" : "s", list);
    }

    @Subscribe
    public void handleDeleteTaskFailEvent(DeleteTaskFailEvent e) {
        String list = e.getInvalidIds().stream()
            .map(Object::toString)
            .collect(Collectors.joining(", "));
        System.out.printf("Invalid task id%s: %s\n", list.length() == 1 ? "" : "s", list);
    }

    public static void main(String[] args) {
        DummyUI ui = new DummyUI();
        eventBus.register(ui);
        ControlCenter cc = ControlCenter.getInstance();
        cc.handleInput("list tag1 tag2");
        cc.handleInput("lol");
        cc.handleInput("add OMG");
        cc.handleInput("list");
        cc.handleInput("rename 1 LOL");
        cc.handleInput("list");
        cc.handleInput("delete 1 2 3");
        cc.handleInput("exit");
    }

}
