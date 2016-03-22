package jfdi.logic.commands;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import jfdi.logic.events.AddTaskDoneEvent;
import jfdi.logic.events.AddTaskFailedEvent;
import jfdi.logic.interfaces.Command;
import jfdi.storage.apis.TaskAttributes;
import jfdi.storage.exceptions.InvalidIdException;
import jfdi.storage.exceptions.InvalidTaskParametersException;
import jfdi.storage.exceptions.NoAttributesChangedException;

/**
 * @author Liu Xinan
 */
public class AddTaskCommand extends Command {

    private String description;
    private Optional<LocalDateTime> startDateTime;
    private Optional<LocalDateTime> endDateTime;
    private Duration[] reminders;
    private String[] tags;

    private AddTaskCommand(Builder builder) {
        this.description = builder.description;
        this.startDateTime = Optional.ofNullable(builder.startDateTime);
        this.endDateTime = Optional.ofNullable(builder.endDateTime);
        this.reminders = builder.reminders.toArray(new Duration[0]);
        this.tags = builder.tags.toArray(new String[0]);
    }

    public static class Builder {

        String description;
        LocalDateTime startDateTime;
        LocalDateTime endDateTime;
        ArrayList<Duration> reminders = new ArrayList<>();
        ArrayList<String> tags = new ArrayList<>();

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

        public Builder addReminder(Duration reminder) {
            this.reminders.add(reminder);
            return this;
        }

        public Builder addReminders(Collection<Duration> reminders) {
            this.reminders.addAll(reminders);
            return this;
        }

        public Builder setReminders(Collection<Duration> reminders) {
            this.reminders = new ArrayList<>(reminders);
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

        public Builder setTags(Collection<String> tags) {
            this.tags = new ArrayList<>(tags);
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
        startDateTime.ifPresent(start -> task.setStartDateTime(start));
        endDateTime.ifPresent(end -> task.setEndDateTime(end));
        task.setReminders(reminders);
        task.setTags(tags);
        try {
            task.save();
            eventBus.post(new AddTaskDoneEvent(task));
        } catch (InvalidTaskParametersException e) {
            eventBus.post(new AddTaskFailedEvent(
                AddTaskFailedEvent.Error.EMPTY_DESCRIPTION));
        } catch (NoAttributesChangedException e) {
            // Should not happen for creating tasks
            assert false;
        } catch (InvalidIdException e) {
            // Should not happen for creating tasks
            assert false;
        }
    }
}
