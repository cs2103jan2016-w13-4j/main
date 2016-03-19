package jfdi.storage.apis;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.TreeSet;

import jfdi.storage.Constants;
import jfdi.storage.entities.Task;
import jfdi.storage.exceptions.InvalidIdException;
import jfdi.storage.exceptions.InvalidTaskParametersException;
import jfdi.storage.exceptions.NoAttributesChangedException;

public class TaskAttributes {

    private Integer id = null;
    private String description = null;
    private LocalDateTime startDateTime = null;
    private LocalDateTime endDateTime = null;
    private HashSet<String> tags = new HashSet<String>();
    private TreeSet<Duration> reminders = new TreeSet<Duration>();
    private boolean isCompleted = false;

    public TaskAttributes() {
    }

    @SuppressWarnings("unchecked")
    public TaskAttributes(Task task) {
        this.id = task.getId();
        this.description = task.getDescription();
        this.startDateTime = task.getStartDateTime();
        this.endDateTime = task.getEndDateTime();
        this.isCompleted = task.isCompleted();
        if (task.getTags() instanceof HashSet<?>) {
            this.tags = (HashSet<String>) task.getTags().clone();
        }
        if (task.getReminders() instanceof TreeSet<?>) {
            this.reminders = (TreeSet<Duration>) task.getReminders().clone();
        }
    }

    public Integer getId() {
        return id;
    }

    /**
     * This method should only be used internally by the database/test.
     *
     * @param id
     *            the id of the task
     */
    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public HashSet<String> getTags() {
        return tags;
    }

    public void setTags(String... tags) {
        this.tags = new HashSet<String>();
        for (String tag : tags) {
            assert tag != null;
            this.tags.add(tag);
        }
    }

    public boolean addTag(String tag) {
        HashSet<String> tags = this.getTags();
        return tags.add(tag);
    }

    public boolean removeTag(String tag) {
        HashSet<String> tags = this.getTags();
        return tags.remove(tag);
    }

    public boolean hasTag(String tag) {
        HashSet<String> tags = this.getTags();
        return tags.contains(tag);
    }

    public TreeSet<Duration> getReminders() {
        return reminders;
    }

    public void setReminders(Duration... reminders) {
        this.reminders = new TreeSet<Duration>();
        for (Duration reminder : reminders) {
            assert reminder != null;
            this.reminders.add(reminder);
        }
    }

    public boolean addReminder(Duration reminder) {
        TreeSet<Duration> reminders = this.getReminders();
        return reminders.add(reminder);
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    /**
     * This method saves the attributes object into a data store object in the
     * database. The attributes are first validated before the data store object
     * is either created or updated.
     *
     * @throws InvalidTaskParametersException
     *             if the attributes object contains an invalid attribute (e.g.
     *             missing description)
     * @throws NoAttributesChangedException
     *             if the save operation does not change the data store object
     *             in any way
     * @throws InvalidIdException
     *             if the ID stored in the attributes object is invalid (e.g.
     *             null or does not exist)
     */
    public void save() throws InvalidTaskParametersException, NoAttributesChangedException,
            InvalidIdException {
        validateAttributes();
        TaskDb.getInstance().createOrUpdate(this);
    }

    public Task toEntity() {
        return new Task(id, description, startDateTime, endDateTime, tags, reminders);
    }

    /**
     * This method checks if the current Task is valid.
     *
     * @return a boolean indicating if the current task is valid
     */
    public boolean isValid() {
        try {
            validateAttributes();
        } catch (InvalidTaskParametersException e) {
            return false;
        }
        return true;
    }

    private void validateAttributes() throws InvalidTaskParametersException {
        ArrayList<String> errors = new ArrayList<String>();

        if (description == null) {
            errors.add(Constants.MESSAGE_MISSING_DESCRIPTION);
        }

        if (startDateTime != null && endDateTime != null && endDateTime.isBefore(startDateTime)) {
            errors.add(Constants.MESSAGE_INVALID_DATETIME);
        }

        if (errors.size() > 0) {
            throw new InvalidTaskParametersException(errors);
        }
    }

    public boolean equalTo(Task task) {
        return Objects.equals(this.id, task.getId())
                && Objects.equals(this.description, task.getDescription())
                && Objects.equals(this.startDateTime, task.getStartDateTime())
                && Objects.equals(this.endDateTime, task.getEndDateTime())
                && Objects.equals(this.tags, task.getTags())
                && Objects.equals(this.reminders, task.getReminders())
                && this.isCompleted == task.isCompleted();
    }

}
