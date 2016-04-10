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

    public LocalDateTime getOldStartDateTime() {
        return oldStartDateTime;
    }

    public LocalDateTime getOldEndDateTime() {
        return oldEndDateTime;
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

            logger.info("Task #" + taskId + " rescheduled.");
        } catch (InvalidIdException e) {
            eventBus.post(new RescheduleTaskFailedEvent(screenId, startDateTime, endDateTime,
                RescheduleTaskFailedEvent.Error.NON_EXISTENT_ID));

            logger.warning("Reschedule failed: Invalid id");
        } catch (InvalidTaskParametersException e) {
            // Should not happen
            assert false;
        } catch (NoAttributesChangedException e) {
            eventBus.post(new RescheduleTaskFailedEvent(screenId, startDateTime, endDateTime,
                RescheduleTaskFailedEvent.Error.NO_CHANGES));

            logger.warning("Reschedule failed: Date time not changed");
        } catch (DuplicateTaskException e) {
            eventBus.post(new RescheduleTaskFailedEvent(screenId, startDateTime, endDateTime,
                RescheduleTaskFailedEvent.Error.DUPLICATED_TASK));

            logger.warning("Reschedule failed: Duplicate task");
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

            logger.info("Undo rescheduling: rescheduled back #" + taskId);
        } catch (InvalidIdException | InvalidTaskParametersException | NoAttributesChangedException
            | DuplicateTaskException e) {
            assert false;
        }
    }

    private void shiftStartAndEndDateTimes(TaskAttributes task) {
        LocalDateTime taskStart = task.getStartDateTime();
        LocalDateTime taskEnd = task.getEndDateTime();

        if (taskStart == null && taskEnd == null) {
            startDateTime = shiftedDateTime;
            endDateTime = null;

            logger.info("Shifting a floating task to a point task.");
        } else if (taskStart != null && taskEnd == null) {
            startDateTime = getShiftedDateTime(task.getStartDateTime());
            endDateTime = null;

            logger.info("Shifting point task.");
        } else if (taskStart == null) {
            startDateTime = null;
            endDateTime = getShiftedDateTime(task.getEndDateTime());

            logger.info("Shifting deadline task.");
        } else {
            Duration eventDuration = Duration.between(taskStart, taskEnd);
            startDateTime = getShiftedDateTime(task.getStartDateTime());
            endDateTime = startDateTime.plus(eventDuration);

            logger.info("Shifting event task.");
        }
    }

    public LocalDateTime getShiftedDateTime() {
        return shiftedDateTime;
    }

    private LocalDateTime getShiftedDateTime(LocalDateTime dateTime) {
        // At least one of them must be true
        if (!isShiftedTimeSpecified) {
            return shiftDate(dateTime);
        } else if (!isShiftedDateSpecified) {
            return shiftTime(dateTime);
        } else {
            return shiftedDateTime;
        }
    }

    private LocalDateTime shiftTime(LocalDateTime originalDateTime) {
        return LocalDateTime.of(originalDateTime.toLocalDate(), shiftedDateTime.toLocalTime());
    }

    private LocalDateTime shiftDate(LocalDateTime originalDateTime) {
        return LocalDateTime.of(shiftedDateTime.toLocalDate(), originalDateTime.toLocalTime());
    }

}
