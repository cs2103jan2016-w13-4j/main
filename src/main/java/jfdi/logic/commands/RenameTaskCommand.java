// @@author A0130195M

package jfdi.logic.commands;

import jfdi.logic.events.RenameTaskDoneEvent;
import jfdi.logic.events.RenameTaskFailedEvent;
import jfdi.logic.interfaces.Command;
import jfdi.storage.apis.TaskAttributes;
import jfdi.storage.exceptions.DuplicateTaskException;
import jfdi.storage.exceptions.InvalidIdException;
import jfdi.storage.exceptions.InvalidTaskParametersException;
import jfdi.storage.exceptions.NoAttributesChangedException;

/**
 * @author Liu Xinan
 */
public class RenameTaskCommand extends Command {

    private int screenId;
    private String description;
    private String oldDescription;

    private RenameTaskCommand(Builder builder) {
        this.screenId = builder.screenId;
        this.description = builder.description;
    }

    public int getScreenId() {
        return screenId;
    }

    public String getDescription() {
        return description;
    }

    public String getOldDescription() {
        return oldDescription;
    }

    public static class Builder {

        int screenId = -1;
        String description;

        public Builder setId(int screenId) {
            this.screenId = screenId;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public RenameTaskCommand build() {
            return new RenameTaskCommand(this);
        }

    }

    @Override
    public void execute() {
        int taskId = ui.getTaskId(screenId);

        try {
            TaskAttributes task = taskDb.getById(taskId);
            oldDescription = task.getDescription();

            task.setDescription(description);
            task.save();

            pushToUndoStack();
            eventBus.post(new RenameTaskDoneEvent(task));

            logger.info("Task #" + taskId + " renamed to " + description);
        } catch (InvalidIdException e) {
            eventBus.post(new RenameTaskFailedEvent(screenId, description,
                          RenameTaskFailedEvent.Error.NON_EXISTENT_ID));

            logger.warning("Rename task failed: Invalid task id");
        } catch (NoAttributesChangedException e) {
            eventBus.post(new RenameTaskFailedEvent(screenId, description,
                          RenameTaskFailedEvent.Error.NO_CHANGES));

            logger.warning("Rename task failed: Task description not changed");
        } catch (DuplicateTaskException e) {
            eventBus.post(new RenameTaskFailedEvent(screenId, description,
                          RenameTaskFailedEvent.Error.DUPLICATED_TASK));

            logger.warning("Rename task failed: Duplicate task");
        } catch (InvalidTaskParametersException e) {
            // Should not happen
            assert false;
        }
    }

    @Override
    public void undo() {
        int taskId = ui.getTaskId(screenId);

        try {
            TaskAttributes task = taskDb.getById(taskId);

            task.setDescription(oldDescription);
            task.save();

            logger.info("Undo renaming: renaming task #" + taskId + " back to " + oldDescription);
        } catch (InvalidIdException | NoAttributesChangedException | InvalidTaskParametersException
               | DuplicateTaskException e) {
            assert false;
        }
    }
}
