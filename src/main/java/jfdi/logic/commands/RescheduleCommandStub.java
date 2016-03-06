package jfdi.logic.commands;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import jfdi.logic.interfaces.AbstractCommand;

/**
 * @author Liu Xinan
 */
public class RescheduleCommandStub extends AbstractCommand {

    private String taskId;
    private Optional<LocalDateTime> startDateTime;
    private Optional<LocalDateTime> endDateTime;
    private Duration[] reminders;
    private String[] tags;
    private boolean isTimeSpecified;

    private RescheduleCommandStub(Builder builder) {
        this.taskId = builder.taskId;
        this.startDateTime = Optional.ofNullable(builder.startDateTime);
        this.endDateTime = Optional.ofNullable(builder.endDateTime);
        this.reminders = builder.reminders.toArray(new Duration[0]);
        this.tags = builder.tags.toArray(new String[0]);
        this.isTimeSpecified = builder.isTimeSpecified;
    }

    public static class Builder {

        String taskId;
        LocalDateTime startDateTime;
        LocalDateTime endDateTime;
        boolean isTimeSpecified;
        ArrayList<Duration> reminders = new ArrayList<>();
        ArrayList<String> tags = new ArrayList<>();

        public Builder setTaskId(String taskId) {
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

        public Builder setIsTimeSpecified(boolean isTimeSpecified) {
            this.isTimeSpecified = isTimeSpecified;
            return this;
        }

        public Builder addReminder(Duration reminder) {
            this.reminders.add(reminder);
            return this;
        }

        public Builder addReminders(Collection<Duration> reminders) {
            this.reminders.addAll(reminders);
            return this;
        }

        public Builder addTag(String tag) {
            this.tags.add(tag);
            return this;
        }

        public Builder addTags(Collection<String> tags) {
            this.tags.addAll(tags);
            return this;
        }

        public RescheduleCommandStub build() {
            return new RescheduleCommandStub(this);
        }

    }

    @Override
    public void execute() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void onSuccess() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void onFailure() {
        // TODO Auto-generated method stub

    }
}
