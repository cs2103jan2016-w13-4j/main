package jfdi.logic.commands;

import jfdi.logic.events.UnmarkTaskDoneEvent;
import jfdi.logic.events.UnmarkTaskFailEvent;
import jfdi.logic.interfaces.Command;
import jfdi.storage.apis.TaskAttributes;
import jfdi.storage.apis.TaskDb;
import jfdi.storage.exceptions.InvalidIdException;
import jfdi.storage.exceptions.NoAttributesChangedException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @author Liu Xinan
 */
public class UnmarkTaskCommand extends Command {

    private ArrayList<Integer> taskIds;

    private UnmarkTaskCommand(Builder builder) {
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

        public UnmarkTaskCommand build() {
            return new UnmarkTaskCommand(this);
        }

    }

    @Override
    public void execute() {
        TaskDb taskdb = TaskDb.getInstance();

        ArrayList<Integer> invalidIds = taskIds.stream()
            .filter(id -> !taskdb.hasId(id))
            .collect(Collectors.toCollection(ArrayList::new));

        if (invalidIds.isEmpty()) {
            ArrayList<TaskAttributes> unmarkedTasks = new ArrayList<>();
            taskIds.stream().forEach(id -> {
                try {
                    taskdb.markAsIncomplete(id);
                    unmarkedTasks.add(taskdb.getById(id));
                } catch (NoAttributesChangedException e) {
                    // Ignore
                } catch (InvalidIdException e) {
                    // Should not happen!
                    e.printStackTrace();
                }
            });
            eventBus.post(new UnmarkTaskDoneEvent(taskIds, unmarkedTasks));
        } else {
            eventBus.post(new UnmarkTaskFailEvent(taskIds, invalidIds));
        }
    }
}
