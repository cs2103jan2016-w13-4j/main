//@@author A0121621Y

package jfdi.storage.apis;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

import jfdi.storage.Constants;
import jfdi.storage.entities.Task;
import jfdi.storage.exceptions.DuplicateTaskException;
import jfdi.storage.exceptions.InvalidIdException;
import jfdi.storage.exceptions.InvalidTaskParametersException;
import jfdi.storage.exceptions.NoAttributesChangedException;

/**
 * This is the data transfer class of the Task entity.
 *
 * @author Thng Kai Yuan
 *
 */
public class TaskAttributes implements Comparable<TaskAttributes> {

    // Attributes of a Task
    private Integer id = null;
    private String description = null;
    private LocalDateTime startDateTime = null;
    private LocalDateTime endDateTime = null;
    private boolean isCompleted = false;

    public TaskAttributes() {}

    public TaskAttributes(Task task) {
        this.id = task.getId();
        this.description = task.getDescription();
        this.startDateTime = task.getStartDateTime();
        this.endDateTime = task.getEndDateTime();
        this.isCompleted = task.isCompleted();
    }

    public Integer getId() {
        return id;
    }

    /**
     * This method should only be used internally by the database/test.
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
     * @throws DuplicateTaskException
     *             if a similar task already exists in the database
     */
    public void save() throws InvalidTaskParametersException, NoAttributesChangedException,
            InvalidIdException, DuplicateTaskException {
        validateAttributes();
        TaskDb.getInstance().createOrUpdate(this);
    }

    /**
     * Checks if the TaskAttributes is overdue.
     *
     * @return a boolean indicating if the TaskAttributes is overdue
     */
    public boolean isOverdue() {
        return !isCompleted() && getStartElseEndDate().isBefore(LocalDateTime.now());
    }

    /**
     * Checks if the TaskAttributes is upcoming.
     *
     * @return a boolean indicating if the TaskAttributes is upcoming
     */
    public boolean isUpcoming() {
        return !isCompleted() && !isOverdue()
                && getStartElseEndDate().isBefore(Constants.LOCALDATETIME_UPCOMING);
    }

    /**
     * @return the start date-time if it is not null, otherwise the end date-time
     */
    private LocalDateTime getStartElseEndDate() {
        if (getStartDateTime() != null) {
            return getStartDateTime();
        }
        return getEndDateTime();
    }

    /**
     * Converts the current TaskAttributes to its corresponding Task entity.
     *
     * @return the corresponding Task entity
     */
    public Task toEntity() {
        return new Task(id, description, startDateTime, endDateTime);
    }

    /**
     * This method checks if the current Task is valid.
     *
     * @return a boolean indicating if the current task is valid
     */
    public boolean isValid() {
        try {
            validateAttributes();
            return true;
        } catch (InvalidTaskParametersException e) {
            return false;
        }
    }

    /**
     * This method validates the existing TaskAttributes.
     *
     * @throws InvalidTaskParametersException
     *             if the existing TaskAttributes contains invalid parameters
     */
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

    /**
     * This method allows one to compare a TaskAttributes with a Task.
     *
     * @param task
     *            the Task to be compared with
     * @return a boolean indicating if the existing TaskAttributes has the same
     *         attributes as the Task compared
     */
    public boolean equalTo(Task task) {
        assert task != null;
        return Objects.equals(this.id, task.getId()) && similarTo(task);
    }

    /**
     * This method allows one to compare the properties of a TaskAttributes with a Task.
     *
     * @param task
     *            the Task to be compared with
     * @return a boolean indicating if the existing TaskAttributes has the same
     *         properties (excluding ID) as the Task compared
     */
    public boolean similarTo(Task task) {
        assert task != null;
        return Objects.equals(this.description, task.getDescription())
                && Objects.equals(this.startDateTime, task.getStartDateTime())
                && Objects.equals(this.endDateTime, task.getEndDateTime())
                && this.isCompleted == task.isCompleted();
    }

    @Override
    public int hashCode() {
        return getStartElseEndDate().hashCode();
    }

    @Override
    public int compareTo(TaskAttributes taskAttributes) {
        assert getStartElseEndDate() != null && taskAttributes.getStartElseEndDate() != null;
        return getStartElseEndDate().compareTo(taskAttributes.getStartElseEndDate());
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || !(object instanceof TaskAttributes)) {
            return false;
        }
        if (object == this) {
            return true;
        }

        TaskAttributes taskAttributes = (TaskAttributes) object;
        assert getStartElseEndDate() != null && taskAttributes.getStartElseEndDate() != null;
        return this.getStartElseEndDate().equals(taskAttributes.getStartElseEndDate());
    }

}
