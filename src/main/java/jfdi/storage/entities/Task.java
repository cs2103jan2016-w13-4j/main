package jfdi.storage.entities;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.TreeSet;

import jfdi.storage.apis.TaskAttributes;

/**
 * This is the Task entity class.
 *
 * @author Thng Kai Yuan
 *
 */
public class Task {

    private Integer id = null;
    private String description = null;
    private LocalDateTime startDateTime = null;
    private LocalDateTime endDateTime = null;
    private HashSet<String> tags = new HashSet<String>();
    private TreeSet<Duration> reminders = new TreeSet<Duration>();
    private boolean isCompleted = false;

    public Task(Integer id, String description, LocalDateTime startDateTime, LocalDateTime endDateTime,
            HashSet<String> tags, TreeSet<Duration> reminders) {
        this.id = id;
        this.description = description;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.tags = tags;
        this.reminders = reminders;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public HashSet<String> getTags() {
        return tags;
    }

    public TreeSet<Duration> getReminders() {
        return reminders;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    /**
     * This method copies the attributes from taskAttributes onto itself.
     *
     * @param taskAttributes
     *            the data transfer object that we want to copy the attributes from
     */
    public void update(TaskAttributes taskAttributes) {
        assert this.getId() == taskAttributes.getId();
        this.description = taskAttributes.getDescription();
        this.startDateTime = taskAttributes.getStartDateTime();
        this.endDateTime = taskAttributes.getEndDateTime();
        this.tags = taskAttributes.getTags();
        this.reminders = taskAttributes.getReminders();
        this.isCompleted = taskAttributes.isCompleted();
    }
}
