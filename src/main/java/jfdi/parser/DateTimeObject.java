package jfdi.parser;

import java.time.LocalDateTime;

import jfdi.parser.Constants.TaskType;

public class DateTimeObject {

    private TaskType taskType;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private boolean isTimeSpecified;

    public DateTimeObject(DateTimeObjectBuilder builder) {
        this.taskType = builder.taskType;
        this.startDateTime = builder.startDateTime;
        this.endDateTime = builder.endDateTime;
        this.isTimeSpecified = builder.isTimeSpecified;
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

    public boolean isTimeSpecified() {
        return isTimeSpecified;
    }

    public static class DateTimeObjectBuilder {
        private TaskType taskType = null;
        private LocalDateTime startDateTime = null;
        private LocalDateTime endDateTime = null;
        private boolean isTimeSpecified = false;

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

        public DateTimeObjectBuilder setIsTimeSpecified(boolean isTimeSpecified) {
            this.isTimeSpecified = isTimeSpecified;
            return this;
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

        public boolean isTimeSpecified() {
            return isTimeSpecified;
        }

        public DateTimeObject build() {
            return new DateTimeObject(this);
        }
    }
}
