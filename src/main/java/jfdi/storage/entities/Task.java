package jfdi.storage.entities;

import java.time.LocalDateTime;

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
    private boolean isCompleted = false;

    public Task(Integer id, String description, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.id = id;
        this.description = description;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
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
        this.isCompleted = taskAttributes.isCompleted();
    }
}
