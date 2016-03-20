package jfdi.logic.commands;

import jfdi.common.utilities.JfdiLogger;
import jfdi.logic.events.MarkTaskDoneEvent;
import jfdi.logic.events.MarkTaskFailedEvent;
import jfdi.logic.interfaces.Command;
import jfdi.storage.apis.TaskAttributes;
import jfdi.storage.apis.TaskDb;
import jfdi.storage.exceptions.InvalidIdException;
import jfdi.storage.exceptions.NoAttributesChangedException;
import jfdi.ui.UI;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author Liu Xinan
 */
public class MarkTaskCommand extends Command {

    private static final Logger LOGGER = JfdiLogger.getLogger();

    private ArrayList<Integer> screenIds;

    private MarkTaskCommand(Builder builder) {
        this.screenIds = builder.screenIds;
    }

    public static class Builder {

        ArrayList<Integer> screenIds = new ArrayList<>();

        public Builder addTaskId(int id) {
            screenIds.add(id);
            return this;
        }

        public Builder addTaskIds(Collection<Integer> ids) {
            screenIds.addAll(ids);
            return this;
        }

        public MarkTaskCommand build() {
            return new MarkTaskCommand(this);
        }

    }

    @Override
    public void execute() {
        TaskDb taskdb = TaskDb.getInstance();
        ArrayList<Integer> taskIds = screenIds.stream()
            .map(screenId -> UI.getInstance().getTaskId(screenId))
            .collect(Collectors.toCollection(ArrayList::new));

        ArrayList<Integer> invalidIds = taskIds.stream()
            .filter(id -> !taskdb.hasId(id))
            .collect(Collectors.toCollection(ArrayList::new));

        if (invalidIds.isEmpty()) {
            ArrayList<TaskAttributes> markedTasks = new ArrayList<>();
            taskIds.stream().forEach(id -> {
                try {
                    markedTasks.add(taskdb.getById(id));
                    taskdb.markAsComplete(id);
                } catch (NoAttributesChangedException e) {
                    LOGGER.warning("Task " + id + " is already completed.");
                } catch (InvalidIdException e) {
                    // Should not happen!
                    assert false;
                }
            });
            eventBus.post(new MarkTaskDoneEvent(taskIds, markedTasks));
        } else {
            eventBus.post(new MarkTaskFailedEvent(taskIds, invalidIds));
        }
    }
}
