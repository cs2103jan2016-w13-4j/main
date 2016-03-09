package jfdi.storage.entities;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.TreeSet;

import jfdi.storage.apis.TaskAttributes;

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

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

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
