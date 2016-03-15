package jfdi.logic.commands;

import jfdi.logic.events.MarkTaskDoneEvent;
import jfdi.logic.events.MarkTaskFailEvent;
import jfdi.logic.interfaces.Command;
import jfdi.storage.apis.TaskAttributes;
import jfdi.storage.apis.TaskDb;
import jfdi.storage.entities.Task;
import jfdi.storage.exceptions.InvalidIdException;
import jfdi.storage.exceptions.NoAttributesChangedException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @author Liu Xinan
 */
public class MarkTaskCommand extends Command {

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
                    taskdb.markAsComplete(id);
                    markedTasks.add(taskdb.getById(id));
                } catch (NoAttributesChangedException e) {
                    // Ignore
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
