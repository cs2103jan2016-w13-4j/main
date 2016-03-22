package jfdi.logic.commands;

import jfdi.logic.events.DeleteTaskDoneEvent;
import jfdi.logic.events.DeleteTaskFailedEvent;
import jfdi.logic.interfaces.Command;
import jfdi.storage.apis.TaskAttributes;
import jfdi.storage.apis.TaskDb;
import jfdi.storage.exceptions.InvalidIdException;
import jfdi.storage.exceptions.InvalidTaskParametersException;
import jfdi.storage.exceptions.NoAttributesChangedException;
import jfdi.ui.UI;

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
        TaskDb taskDb = TaskDb.getInstance();
        ArrayList<Integer> taskIds = screenIds.stream()
            .map(screenId -> UI.getInstance().getTaskId(screenId))
            .collect(Collectors.toCollection(ArrayList::new));

        ArrayList<Integer> invalidIds = taskIds.stream()
            .filter(id -> !taskDb.hasId(id))
            .collect(Collectors.toCollection(ArrayList::new));

        if (invalidIds.isEmpty()) {
            deletedTasks = new ArrayList<>();
            taskIds.forEach(id -> {
                try {
                    deletedTasks.add(taskDb.getById(id));
                    TaskDb.getInstance().destroy(id);
                } catch (InvalidIdException e) {
                    // Should not happen
                    assert false;
                }
            });

            pushToUndoStack();
            eventBus.post(new DeleteTaskDoneEvent(taskIds, deletedTasks));
        } else {
            eventBus.post(new DeleteTaskFailedEvent(invalidIds));
        }
    }

    @Override
    public void undo() {
        deletedTasks.stream()
            .forEach(task -> {
                try {
                    task.save();
                } catch (InvalidTaskParametersException | InvalidIdException | NoAttributesChangedException e) {
                    assert false;
                }
            });

        pushToRedoStack();
    }
}
