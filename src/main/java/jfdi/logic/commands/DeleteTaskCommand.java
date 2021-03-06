// @@author A0130195M

package jfdi.logic.commands;

import jfdi.logic.events.DeleteTaskDoneEvent;
import jfdi.logic.events.DeleteTaskFailedEvent;
import jfdi.logic.interfaces.Command;
import jfdi.storage.apis.TaskAttributes;
import jfdi.storage.exceptions.DuplicateTaskException;
import jfdi.storage.exceptions.InvalidIdException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @author Liu Xinan
 */
public class DeleteTaskCommand extends Command {

    private ArrayList<Integer> screenIds;
    private ArrayList<TaskAttributes> deletedTasks;

    private DeleteTaskCommand(Builder builder) {
        this.screenIds = builder.screenIds;
    }

    public ArrayList<Integer> getScreenIds() {
        return screenIds;
    }

    public ArrayList<TaskAttributes> getDeletedTasks() {
        return deletedTasks;
    }

    public static class Builder {

        ArrayList<Integer> screenIds = new ArrayList<>();

        public Builder addId(int id) {
            screenIds.add(id);
            return this;
        }

        public Builder addIds(Collection<Integer> ids) {
            screenIds.addAll(ids);
            return this;
        }

        public DeleteTaskCommand build() {
            return new DeleteTaskCommand(this);
        }

    }



    @Override
    public void execute() {
        ArrayList<Integer> taskIds = screenIds.stream().map(ui::getTaskId)
            .collect(Collectors.toCollection(ArrayList::new));

        ArrayList<Integer> invalidIds = screenIds.stream()
            .filter(id -> !taskDb.hasId(ui.getTaskId(id)))
            .collect(Collectors.toCollection(ArrayList::new));

        if (invalidIds.isEmpty()) {
            deletedTasks = new ArrayList<>();
            taskIds.forEach(id -> {
                try {
                    deletedTasks.add(taskDb.getById(id));
                    taskDb.destroy(id);
                    logger.info("Task deleted: #" + id);
                } catch (InvalidIdException e) {
                    // Should not happen
                    assert false;
                }
            });

            pushToUndoStack();
            eventBus.post(new DeleteTaskDoneEvent(screenIds, deletedTasks));
        } else {
            eventBus.post(new DeleteTaskFailedEvent(invalidIds));
        }
    }

    @Override
    public void undo() {
        deletedTasks.stream().forEach(task -> {
            try {
                taskDb.undestroy(task.getId());

                logger.info("Undo deleting task: #" + task.getId());
            } catch (InvalidIdException | DuplicateTaskException e) {
                assert false;
            }
        });
    }
}
