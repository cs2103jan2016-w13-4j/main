package jfdi.logic.commands;

import jfdi.logic.events.AddTaskDoneEvent;
import jfdi.logic.events.AddTaskFailEvent;
import jfdi.logic.interfaces.Command;
import jfdi.storage.data.TaskAttributes;
import jfdi.storage.exceptions.InvalidIdException;
import jfdi.storage.exceptions.InvalidTaskParametersException;
import jfdi.storage.exceptions.NoAttributesChangedException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Liu Xinan
 */
public class AddTaskCommand extends Command {

    private String description;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private Duration[] reminders;
    private String[] tags;

    private AddTaskCommand(Builder builder) {
        this.description = builder.description;
        this.startDateTime = builder.startDateTime;
        this.endDateTime = builder.endDateTime;
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

        public Builder addTag(String tag) {
            this.tags.add(tag);
            return this;
        }

        public Builder addTags(Collection<String> tags) {
            this.tags.addAll(tags);
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
        task.setStartDateTime(startDateTime);
        task.setEndDateTime(endDateTime);
        task.setReminders(reminders);
        task.setTags(tags);
        try {
            task.save();
            eventBus.post(new AddTaskDoneEvent(task));
        } catch (InvalidTaskParametersException e) {
            eventBus.post(new AddTaskFailEvent(AddTaskFailEvent.Error.EMPTY_DESCRIPTION));
        } catch (NoAttributesChangedException e) {
            // Should not happen for creating tasks
            e.printStackTrace();
        } catch (InvalidIdException e) {
            // Should not happen for creating tasks
            e.printStackTrace();
        }
    }
}
