package jfdi.logic.commands;

import java.time.Duration;
import java.time.LocalDateTime;

import jfdi.logic.events.RescheduleTaskDoneEvent;
import jfdi.logic.events.RescheduleTaskFailedEvent;
import jfdi.logic.interfaces.Command;
import jfdi.storage.apis.TaskAttributes;
import jfdi.storage.apis.TaskDb;
import jfdi.storage.exceptions.DuplicateTaskException;
import jfdi.storage.exceptions.InvalidIdException;
import jfdi.storage.exceptions.InvalidTaskParametersException;
import jfdi.storage.exceptions.NoAttributesChangedException;
import jfdi.ui.UI;

/**
 * @author Liu Xinan
 */
public class RescheduleTaskCommand extends Command {

    private int screenId;
    private boolean isShiftedDateSpecified;
    private boolean isShiftedTimeSpecified;
    private LocalDateTime shiftedDateTime;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private LocalDateTime oldStartDateTime;
    private LocalDateTime oldEndDateTime;

    private RescheduleTaskCommand(Builder builder) {
        this.screenId = builder.screenId;
        this.isShiftedDateSpecified = builder.isShiftedDateSpecified;
        this.isShiftedTimeSpecified = builder.isShiftedTimeSpecified;
        this.shiftedDateTime = builder.shiftedDateTime;
        this.startDateTime = builder.startDateTime;
        this.endDateTime = builder.endDateTime;
    }

    public static class Builder {

        int screenId;
        boolean isShiftedDateSpecified = false;
        boolean isShiftedTimeSpecified = false;
        LocalDateTime shiftedDateTime;
        LocalDateTime startDateTime;
        LocalDateTime endDateTime;

        public Builder setId(int screenId) {
            this.screenId = screenId;
            return this;
        }

        public void setShiftedDateSpecified(boolean isShiftedDateSpecified) {
            this.isShiftedDateSpecified = isShiftedDateSpecified;
        }

        public void setShiftedTimeSpecified(boolean isShiftedTimeSpecified) {
            this.isShiftedTimeSpecified = isShiftedTimeSpecified;
        }

        public Builder setShiftedDateTime(LocalDateTime shiftedDateTime) {
            this.shiftedDateTime = shiftedDateTime;
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
        int taskId = UI.getInstance().getTaskId(screenId);

        try {
            TaskAttributes task = TaskDb.getInstance().getById(taskId);

            oldStartDateTime = task.getStartDateTime();
            oldEndDateTime = task.getEndDateTime();

            if (shiftedDateTime != null) {
                shiftStartAndEndDateTimes(task);
            }

            task.setStartDateTime(startDateTime);
            task.setEndDateTime(endDateTime);
            task.save();

            pushToUndoStack();
            eventBus.post(new RescheduleTaskDoneEvent(task));
        } catch (InvalidIdException e) {
            eventBus.post(new RescheduleTaskFailedEvent(screenId,
                startDateTime, endDateTime,
                RescheduleTaskFailedEvent.Error.NON_EXISTENT_ID));
        } catch (InvalidTaskParametersException e) {
            // Should not happen
            assert false;
        } catch (NoAttributesChangedException e) {
            eventBus.post(new RescheduleTaskFailedEvent(screenId,
                startDateTime, endDateTime,
                RescheduleTaskFailedEvent.Error.NO_CHANGES));
        } catch (DuplicateTaskException e) {
            eventBus.post(new RescheduleTaskFailedEvent(screenId,
                startDateTime, endDateTime,
                RescheduleTaskFailedEvent.Error.DUPLICATED_TASK));
        }
    }

    @Override
    public void undo() {
        int taskId = UI.getInstance().getTaskId(screenId);

        try {
            TaskAttributes task = TaskDb.getInstance().getById(taskId);

            task.setStartDateTime(oldStartDateTime);
            task.setEndDateTime(oldEndDateTime);
            task.save();

            pushToRedoStack();
        } catch (InvalidIdException | InvalidTaskParametersException
            | NoAttributesChangedException | DuplicateTaskException e) {
            assert false;
        }
    }

    private void shiftStartAndEndDateTimes(TaskAttributes task) {
        assert shiftedDateTime != null;
        assert isShiftedDateSpecified || isShiftedTimeSpecified;
        LocalDateTime taskStart = task.getStartDateTime();
        LocalDateTime taskEnd = task.getEndDateTime();

        // Set floating task to point task
        if (taskStart == null && taskEnd == null) {
            startDateTime = shiftedDateTime;
            endDateTime = null;

            // Shift point task
        } else if (taskStart != null && taskEnd == null) {
            startDateTime = getShiftedDateTime(task.getStartDateTime());
            endDateTime = null;

            // Shift deadline task
        } else if (taskStart == null && taskEnd != null) {
            startDateTime = null;
            endDateTime = getShiftedDateTime(task.getEndDateTime());

            // Shift event, preserving duration
        } else {
            Duration eventDuration = Duration.between(taskStart, taskEnd);
            startDateTime = getShiftedDateTime(task.getStartDateTime());
            endDateTime = startDateTime.plus(eventDuration);
        }
    }

    private LocalDateTime getShiftedDateTime(LocalDateTime dateTime) {
        if (isShiftedDateSpecified && isShiftedTimeSpecified) {
            return shiftedDateTime;
        } else if (isShiftedDateSpecified && !isShiftedTimeSpecified) {
            return shiftDate(dateTime);
        } else if (!isShiftedDateSpecified && isShiftedTimeSpecified) {
            return shiftTime(dateTime);
        }
        assert false;
        return null;
    }

    private LocalDateTime shiftTime(LocalDateTime originalDateTime) {
        assert originalDateTime != null && shiftedDateTime != null;
        return LocalDateTime.of(originalDateTime.toLocalDate(),
            shiftedDateTime.toLocalTime());
    }

    private LocalDateTime shiftDate(LocalDateTime originalDateTime) {
        assert originalDateTime != null && shiftedDateTime != null;
        return LocalDateTime.of(shiftedDateTime.toLocalDate(),
            originalDateTime.toLocalTime());
    }

}
