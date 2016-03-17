package jfdi.logic.commands;

import jfdi.logic.events.DeleteTaskDoneEvent;
import jfdi.logic.events.DeleteTaskFailEvent;
import jfdi.logic.interfaces.Command;
import jfdi.storage.apis.TaskAttributes;
import jfdi.storage.apis.TaskDb;
import jfdi.storage.exceptions.InvalidIdException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @author Liu Xinan
 */
public class DeleteTaskCommand extends Command {

    private ArrayList<Integer> taskIds;

    private DeleteTaskCommand(Builder builder) {
        this.taskIds = builder.taskIds;
    }

    public static class Builder {

        ArrayList<Integer> taskIds = new ArrayList<>();

        public Builder addId(int id) {
            taskIds.add(id);
            return this;
        }

        public Builder addIds(Collection<Integer> ids) {
            taskIds.addAll(ids);
            return this;
        }

        public DeleteTaskCommand build() {
            return new DeleteTaskCommand(this);
        }

    }

    @Override
    public void execute() {
        TaskDb taskDb = TaskDb.getInstance();

        ArrayList<Integer> invalidIds = taskIds.stream()
            .filter(id -> !taskDb.hasId(id))
            .collect(Collectors.toCollection(ArrayList::new));

        if (invalidIds.isEmpty()) {
            ArrayList<TaskAttributes> deletedTasks = new ArrayList<>();
            taskIds.forEach(id -> {
                try {
                    deletedTasks.add(taskDb.getById(id));
                    TaskDb.getInstance().destroy(id);
                } catch (InvalidIdException e) {
                    // Should not happen
                    assert false;
                }
            });
            eventBus.post(new DeleteTaskDoneEvent(taskIds, deletedTasks));
        } else {
            eventBus.post(new DeleteTaskFailEvent(invalidIds));
        }
    }
}
