package dummy;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import jfdi.logic.ControlCenter;
import jfdi.logic.events.*;
import jfdi.storage.data.TaskAttributes;

/**
 * Example of using EventBus and Commands
 *
 * @author Liu Xinan
 */
public class DummyUI {

    private static final EventBus eventBus = ControlCenter.getEventBus();

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

    public static void main(String[] args) {
        DummyUI ui = new DummyUI();
        eventBus.register(ui);
        ControlCenter cc = ControlCenter.getInstance();
        cc.handleInput("list tag1 tag2");
        cc.handleInput("lol");
        cc.handleInput("add OMG");
        cc.handleInput("list");
        cc.handleInput("exit");
    }

}
