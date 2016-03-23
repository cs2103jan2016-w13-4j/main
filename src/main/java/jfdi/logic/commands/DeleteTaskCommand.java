package jfdi.logic.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import jfdi.logic.events.DeleteTaskDoneEvent;
import jfdi.logic.events.DeleteTaskFailedEvent;
import jfdi.logic.interfaces.Command;
import jfdi.storage.apis.TaskAttributes;
import jfdi.storage.apis.TaskDb;
import jfdi.storage.exceptions.InvalidIdException;
import jfdi.ui.UI;

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
        UI ui = UI.getInstance();

        TaskDb taskDb = TaskDb.getInstance();
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
                    logger.info("Deleting task #" + id);
                    TaskDb.getInstance().destroy(id);
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
                TaskDb.getInstance().undestroy(task.getId());
            } catch (InvalidIdException e) {
                assert false;
            }
        });

        pushToRedoStack();
    }
}
