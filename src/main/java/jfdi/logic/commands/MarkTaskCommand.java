package jfdi.logic.commands;

import jfdi.common.utilities.JfdiLogger;
import jfdi.logic.events.MarkTaskDoneEvent;
import jfdi.logic.events.MarkTaskFailEvent;
import jfdi.logic.interfaces.Command;
import jfdi.storage.apis.TaskAttributes;
import jfdi.storage.apis.TaskDb;
import jfdi.storage.exceptions.InvalidIdException;
import jfdi.storage.exceptions.NoAttributesChangedException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author Liu Xinan
 */
public class MarkTaskCommand extends Command {

    private static final Logger logger = JfdiLogger.getLogger();

    private ArrayList<Integer> taskIds;

    private MarkTaskCommand(Builder builder) {
        this.taskIds = builder.taskIds;
    }

    public static class Builder {

        ArrayList<Integer> taskIds = new ArrayList<>();

        public Builder addTaskId(int id) {
            taskIds.add(id);
            return this;
        }

        public Builder addTaskIds(Collection<Integer> ids) {
            taskIds.addAll(ids);
            return this;
        }

        public MarkTaskCommand build() {
            return new MarkTaskCommand(this);
        }

    }

    @Override
    public void execute() {
        TaskDb taskdb = TaskDb.getInstance();

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
                    logger.warning("Task " + id + " is already completed.");
                } catch (InvalidIdException e) {
                    // Should not happen!
                    e.printStackTrace();
                }
            });
            eventBus.post(new MarkTaskDoneEvent(taskIds, markedTasks));
        } else {
            eventBus.post(new MarkTaskFailEvent(taskIds, invalidIds));
        }
    }
}
