package jfdi.logic.events;

import jfdi.storage.apis.TaskAttributes;

/**
 * @author Liu Xinan
 */
public class RenameTaskDoneEvent {

    private TaskAttributes task;


    public RenameTaskDoneEvent(TaskAttributes task) {
        this.task = task;
    }

    public TaskAttributes getTask() {
        return task;
    }
}
