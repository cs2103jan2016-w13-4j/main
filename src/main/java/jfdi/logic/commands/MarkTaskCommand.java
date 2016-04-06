// @@author A0130195M

package jfdi.logic.commands;

import jfdi.common.utilities.JfdiLogger;
import jfdi.logic.events.MarkTaskDoneEvent;
import jfdi.logic.events.MarkTaskFailedEvent;
import jfdi.logic.interfaces.Command;
import jfdi.storage.apis.TaskAttributes;
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

    private static final Logger LOGGER = JfdiLogger.getLogger();

    private ArrayList<Integer> screenIds;
    private ArrayList<Integer> markedIds;

    private MarkTaskCommand(Builder builder) {
        this.screenIds = builder.screenIds;
    }

    public ArrayList<Integer> getScreenIds() {
        return screenIds;
    }

    public ArrayList<Integer> getMarkedIds() {
        return markedIds;
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
        ArrayList<Integer> taskIds = screenIds.stream().map(ui::getTaskId)
            .collect(Collectors.toCollection(ArrayList::new));

        ArrayList<Integer> invalidIds = screenIds.stream()
            .filter(id -> !taskDb.hasId(ui.getTaskId(id)))
            .collect(Collectors.toCollection(ArrayList::new));

        if (invalidIds.isEmpty()) {
            ArrayList<TaskAttributes> markedTasks = new ArrayList<>();
            markedIds = new ArrayList<>();
            taskIds.stream().forEach(id -> {
                try {
                    markedTasks.add(taskDb.getById(id));
                    taskDb.markAsComplete(id);
                    markedIds.add(id);
                } catch (NoAttributesChangedException e) {
                    LOGGER.warning("Task " + id + " is already completed.");
                } catch (InvalidIdException e) {
                    // Should not happen!
                    assert false;
                }
            });

            pushToUndoStack();
            eventBus.post(new MarkTaskDoneEvent(screenIds, markedTasks));
        } else {
            eventBus.post(new MarkTaskFailedEvent(screenIds, invalidIds));
        }
    }

    @Override
    public void undo() {
        markedIds.stream().forEach(id -> {
            try {
                taskDb.markAsIncomplete(id);
            } catch (NoAttributesChangedException | InvalidIdException e) {
                assert false;
            }
        });

        pushToRedoStack();
    }
}
