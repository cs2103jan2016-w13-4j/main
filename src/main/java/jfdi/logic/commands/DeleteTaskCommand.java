package jfdi.logic.commands;

import jfdi.logic.events.DeleteTaskDoneEvent;
import jfdi.logic.events.DeleteTaskFailEvent;
import jfdi.logic.interfaces.Command;
import jfdi.storage.data.TaskDb;
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
        ArrayList<Integer> invalidIds = taskIds.stream()
                .filter(id -> !TaskDb.hasId(id))
                .collect(Collectors.toCollection(ArrayList::new));

        if (invalidIds.isEmpty()) {
            taskIds.forEach(id -> {
                try {
                    TaskDb.destroy(id);
                } catch (InvalidIdException e) {
                    // Should not happen
                    e.printStackTrace();
                }
            });
            eventBus.post(new DeleteTaskDoneEvent(taskIds));
        } else {
            eventBus.post(new DeleteTaskFailEvent(invalidIds));
        }
    }
}
