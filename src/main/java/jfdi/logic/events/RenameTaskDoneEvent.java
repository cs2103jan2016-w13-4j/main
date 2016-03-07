package jfdi.logic.events;

/**
 * @author Liu Xinan
 */
public class RenameTaskDoneEvent {

    private int taskId;
    private String description;

    public RenameTaskDoneEvent(int taskId, String description) {
        this.taskId = taskId;
        this.description = description;
    }

    public int getTaskId() {
        return taskId;
    }

    public String getDescription() {
        return description;
    }
}
