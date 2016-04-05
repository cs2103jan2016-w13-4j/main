// @@author A0130195M
package jfdi.logic.commands;

import jfdi.logic.events.RescheduleTaskDoneEvent;
import jfdi.logic.events.RescheduleTaskFailedEvent;
import jfdi.logic.interfaces.Command;
import jfdi.storage.apis.TaskAttributes;
import jfdi.storage.exceptions.DuplicateTaskException;
import jfdi.storage.exceptions.InvalidIdException;
import jfdi.storage.exceptions.InvalidTaskParametersException;
import jfdi.storage.exceptions.NoAttributesChangedException;

import java.time.Duration;
import java.time.LocalDateTime;

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

    public int getScreenId() {
        return screenId;
    }

    public boolean isShiftedDateSpecified() {
        return isShiftedDateSpecified;
    }

    public boolean isShiftedTimeSpecified() {
        return isShiftedTimeSpecified;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
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

        public Builder setShiftedDateSpecified(boolean isShiftedDateSpecified) {
            this.isShiftedDateSpecified = isShiftedDateSpecified;
            return this;
        }

        public Builder setShiftedTimeSpecified(boolean isShiftedTimeSpecified) {
            this.isShiftedTimeSpecified = isShiftedTimeSpecified;
            return this;
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
        int taskId = ui.getTaskId(screenId);

        try {
            TaskAttributes task = taskDb.getById(taskId);

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
            eventBus.post(new RescheduleTaskFailedEvent(screenId, startDateTime, endDateTime,
                RescheduleTaskFailedEvent.Error.NON_EXISTENT_ID));
        } catch (InvalidTaskParametersException e) {
            // Should not happen
            assert false;
        } catch (NoAttributesChangedException e) {
            eventBus.post(new RescheduleTaskFailedEvent(screenId, startDateTime, endDateTime,
                RescheduleTaskFailedEvent.Error.NO_CHANGES));
        } catch (DuplicateTaskException e) {
            eventBus.post(new RescheduleTaskFailedEvent(screenId, startDateTime, endDateTime,
                RescheduleTaskFailedEvent.Error.DUPLICATED_TASK));
        }
    }

    @Override
    public void undo() {
        int taskId = ui.getTaskId(screenId);

        try {
            TaskAttributes task = taskDb.getById(taskId);

            task.setStartDateTime(oldStartDateTime);
            task.setEndDateTime(oldEndDateTime);
            task.save();

            pushToRedoStack();
        } catch (InvalidIdException | InvalidTaskParametersException | NoAttributesChangedException
            | DuplicateTaskException e) {
            assert false;
        }
    }

    private void shiftStartAndEndDateTimes(TaskAttributes task) {
        assert shiftedDateTime != null;
        assert isShiftedDateSpecified || isShiftedTimeSpecified;
        LocalDateTime taskStart = task.getStartDateTime();
        LocalDateTime taskEnd = task.getEndDateTime();

        if (taskStart == null && taskEnd == null) {
            // Set floating task to point task
            startDateTime = shiftedDateTime;
            endDateTime = null;

        } else if (taskStart != null && taskEnd == null) {
            // Shift point task
            startDateTime = getShiftedDateTime(task.getStartDateTime());
            endDateTime = null;

        } else if (taskStart == null && taskEnd != null) {
            // Shift deadline task
            startDateTime = null;
            endDateTime = getShiftedDateTime(task.getEndDateTime());

        } else {
            // Shift event, preserving duration
            Duration eventDuration = Duration.between(taskStart, taskEnd);
            startDateTime = getShiftedDateTime(task.getStartDateTime());
            endDateTime = startDateTime.plus(eventDuration);
        }
    }

    public LocalDateTime getShiftedDateTime() {
        return shiftedDateTime;
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
        return LocalDateTime.of(originalDateTime.toLocalDate(), shiftedDateTime.toLocalTime());
    }

    private LocalDateTime shiftDate(LocalDateTime originalDateTime) {
        assert originalDateTime != null && shiftedDateTime != null;
        return LocalDateTime.of(shiftedDateTime.toLocalDate(), originalDateTime.toLocalTime());
    }

}
