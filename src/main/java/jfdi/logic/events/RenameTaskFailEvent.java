package jfdi.logic.events;

/**
 * @author Liu Xinan
 */
public class RenameTaskFailEvent {

    public enum Error {
        NON_EXISTENT_ID, NO_CHANGES, UNKNOWN
    }

    private int taskId;
    private String description;
    private Error error;

    public RenameTaskFailEvent(int taskId, String description, Error error) {
        this.taskId = taskId;
        this.description = description;
    }

    public int getTaskId() {
        return taskId;
    }

    public String getDescription() {
        return description;
    }

    public Error getError() {
        return error;
    }
}
