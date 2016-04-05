// @@author A0130195M
package jfdi.logic.commands;

import jfdi.logic.events.AddTaskDoneEvent;
import jfdi.logic.events.AddTaskFailedEvent;
import jfdi.logic.interfaces.Command;
import jfdi.storage.apis.TaskAttributes;
import jfdi.storage.exceptions.DuplicateTaskException;
import jfdi.storage.exceptions.InvalidIdException;
import jfdi.storage.exceptions.InvalidTaskParametersException;
import jfdi.storage.exceptions.NoAttributesChangedException;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author Liu Xinan
 */
public class AddTaskCommand extends Command {

    private String description;
    private Optional<LocalDateTime> startDateTime;
    private Optional<LocalDateTime> endDateTime;
    private int id = -1;

    private AddTaskCommand(Builder builder) {
        this.description = builder.description;
        this.startDateTime = Optional.ofNullable(builder.startDateTime);
        this.endDateTime = Optional.ofNullable(builder.endDateTime);
    }

    public String getDescription() {
        return description;
    }

    public Optional<LocalDateTime> getStartDateTime() {
        return startDateTime;
    }

    public Optional<LocalDateTime> getEndDateTime() {
        return endDateTime;
    }

    public int getId() {
        return id;
    }

    public static class Builder {

        String description;
        LocalDateTime startDateTime;
        LocalDateTime endDateTime;

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setStartDateTime(LocalDateTime startDateTime) {
            this.startDateTime = startDateTime;
            return this;
        }

        public Builder setEndDateTime(LocalDateTime endDateTime) {
            this.endDateTime = endDateTime;
            return this;
        }

        public AddTaskCommand build() {
            return new AddTaskCommand(this);
        }

    }

    @Override
    public void execute() {
        TaskAttributes task = new TaskAttributes();
        task.setDescription(description);
        startDateTime.ifPresent(task::setStartDateTime);
        endDateTime.ifPresent(task::setEndDateTime);
        try {
            task.save();
            this.id = task.getId();

            pushToUndoStack();
            eventBus.post(new AddTaskDoneEvent(task));
        } catch (InvalidTaskParametersException e) {
            eventBus.post(new AddTaskFailedEvent(
                AddTaskFailedEvent.Error.EMPTY_DESCRIPTION));
        } catch (DuplicateTaskException e) {
            eventBus.post(new AddTaskFailedEvent(
                AddTaskFailedEvent.Error.DUPLICATED_TASK));
        } catch (NoAttributesChangedException | InvalidIdException e) {
            // Should not happen for creating tasks
            assert false;
        }
    }

    @Override
    public void undo() {
        try {
            taskDb.destroy(id);

            pushToRedoStack();
        } catch (InvalidIdException e) {
            // Should not happen for creating tasks
            assert false;
        }
    }

}
