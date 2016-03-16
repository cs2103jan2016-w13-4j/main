package jfdi.logic.commands;

import jfdi.logic.events.RenameTaskDoneEvent;
import jfdi.logic.events.RenameTaskFailEvent;
import jfdi.logic.interfaces.Command;
import jfdi.storage.apis.TaskAttributes;
import jfdi.storage.apis.TaskDb;
import jfdi.storage.exceptions.InvalidIdException;
import jfdi.storage.exceptions.InvalidTaskParametersException;
import jfdi.storage.exceptions.NoAttributesChangedException;

/**
 * @author Liu Xinan
 */
public class RenameTaskCommand extends Command {

    private int taskId;
    private String description;

    private RenameTaskCommand(Builder builder) {
        this.taskId = builder.taskId;
        this.description = builder.description;
    }

    public static class Builder {

        int taskId = -1;
        String description;

        public Builder setId(int taskId) {
            this.taskId = taskId;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public RenameTaskCommand build() {
            if (taskId == -1) {
                throw new IllegalStateException("You must set the task ID for rename!");
            }
            return new RenameTaskCommand(this);
        }

    }

    @Override
    public void execute() {
        try {
            TaskAttributes task = TaskDb.getInstance().getById(taskId);
            task.setDescription(description);
            task.save();
            eventBus.post(new RenameTaskDoneEvent(task));
        } catch (InvalidIdException e) {
            eventBus.post(new RenameTaskFailEvent(taskId, description, RenameTaskFailEvent.Error.NON_EXISTENT_ID));
        } catch (InvalidTaskParametersException e) {
            // Should not happen
            assert false;
        } catch (NoAttributesChangedException e) {
            eventBus.post(new RenameTaskFailEvent(taskId, description, RenameTaskFailEvent.Error.NO_CHANGES));
        }
    }

}
