// @@author A0130195M

package jfdi.logic.commands;

import jfdi.common.utilities.JfdiLogger;
import jfdi.logic.events.UnmarkTaskDoneEvent;
import jfdi.logic.events.UnmarkTaskFailedEvent;
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
public class UnmarkTaskCommand extends Command {

    private static final Logger LOGGER = JfdiLogger.getLogger();

    private ArrayList<Integer> screenIds;
    private ArrayList<Integer> unmarkedIds;

    private UnmarkTaskCommand(Builder builder) {
        this.screenIds = builder.screenIds;
    }

    public ArrayList<Integer> getScreenIds() {
        return screenIds;
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

        public UnmarkTaskCommand build() {
            return new UnmarkTaskCommand(this);
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
            ArrayList<TaskAttributes> unmarkedTasks = new ArrayList<>();
            unmarkedIds = new ArrayList<>();
            taskIds.stream().forEach(id -> {
                try {
                    unmarkedTasks.add(taskDb.getById(id));
                    taskDb.markAsIncomplete(id);

                    unmarkedIds.add(id);
                } catch (NoAttributesChangedException e) {
                    LOGGER.warning("Task " + id + " was not completed.");
                } catch (InvalidIdException e) {
                    assert false;
                }
            });

            pushToUndoStack();
            eventBus.post(new UnmarkTaskDoneEvent(screenIds, unmarkedTasks));
        } else {
            eventBus.post(new UnmarkTaskFailedEvent(screenIds, invalidIds));
        }
    }

    @Override
    public void undo() {
        unmarkedIds.stream().forEach(id -> {
            try {
                taskDb.markAsComplete(id);

                pushToRedoStack();
            } catch (NoAttributesChangedException | InvalidIdException e) {
                assert false;
            }
        });
    }

}
