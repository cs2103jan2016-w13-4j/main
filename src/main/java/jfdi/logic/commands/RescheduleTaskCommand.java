package jfdi.logic.commands;

import jfdi.logic.events.RescheduleTaskDoneEvent;
import jfdi.logic.events.RescheduleTaskFailEvent;
import jfdi.logic.interfaces.Command;
import jfdi.storage.data.TaskAttributes;
import jfdi.storage.data.TaskDb;
import jfdi.storage.exceptions.InvalidIdException;
import jfdi.storage.exceptions.InvalidTaskParametersException;
import jfdi.storage.exceptions.NoAttributesChangedException;

import java.time.LocalDateTime;

/**
 * @author Liu Xinan
 */
public class RescheduleTaskCommand extends Command {

    private int taskId;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    private RescheduleTaskCommand(Builder builder) {
        this.taskId = builder.taskId;
        this.startDateTime = builder.startDateTime;
        this.endDateTime = builder.endDateTime;
    }

    public static class Builder {

        int taskId;
        LocalDateTime startDateTime;
        LocalDateTime endDateTime;

        public Builder setId(int taskId) {
            this.taskId = taskId;
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

        public RescheduleTaskCommand build() {
            return new RescheduleTaskCommand(this);
        }

    }

    @Override
    public void execute() {
        try {
            TaskAttributes task = TaskDb.getById(taskId);
            task.setStartDateTime(startDateTime);
            task.setEndDateTime(endDateTime);
            task.save();
            eventBus.post(new RescheduleTaskDoneEvent(taskId, startDateTime, endDateTime));
        } catch (InvalidIdException e) {
            eventBus.post(new RescheduleTaskFailEvent(taskId, startDateTime, endDateTime,
                RescheduleTaskFailEvent.Error.NON_EXISTENT_ID));
        } catch (InvalidTaskParametersException e) {
            // Should not happen
            e.printStackTrace();
        } catch (NoAttributesChangedException e) {
            eventBus.post(new RescheduleTaskFailEvent(taskId, startDateTime, endDateTime,
                RescheduleTaskFailEvent.Error.NO_CHANGES));
        }

    }
}
