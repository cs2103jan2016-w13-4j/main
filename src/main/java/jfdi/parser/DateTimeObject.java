// @@author A0127393B

package jfdi.parser;

import java.time.LocalDateTime;

import jfdi.parser.Constants.TaskType;

/**
 * The DateTimeObject class encapsulates all the information relevant to the
 * datetime field of a user's input. This includes the task type (floating,
 * event, etc), the values of the start and end date time specified, and so on.
 * Creating a DateTimeObject should only be done through the Builder subclass.
 *
 * @author Leonard Hio
 *
 */
public class DateTimeObject {

    private TaskType taskType;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    public DateTimeObject(DateTimeObjectBuilder builder) {
        this.taskType = builder.taskType;
        this.startDateTime = builder.startDateTime;
        this.endDateTime = builder.endDateTime;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public static class DateTimeObjectBuilder {
        private TaskType taskType = null;
        private LocalDateTime startDateTime = null;
        private LocalDateTime endDateTime = null;

        public DateTimeObjectBuilder setTaskType(TaskType t) {
            this.taskType = t;
            return this;
        }

        public DateTimeObjectBuilder setStartDateTime(LocalDateTime ldt) {
            this.startDateTime = ldt;
            return this;
        }

        public DateTimeObjectBuilder setEndDateTime(LocalDateTime ldt) {
            this.endDateTime = ldt;
            return this;
        }

        public DateTimeObject build() {
            return new DateTimeObject(this);
        }
    }
}
