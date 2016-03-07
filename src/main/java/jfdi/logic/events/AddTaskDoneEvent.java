package jfdi.logic.events;

import jfdi.storage.data.TaskAttributes;

/**
 * @author Liu Xinan
 */
public class AddTaskDoneEvent {

    private TaskAttributes task;

    public AddTaskDoneEvent(TaskAttributes task) {
        this.task = task;
    }

    public TaskAttributes getTask() {
        return task;
    }
}
