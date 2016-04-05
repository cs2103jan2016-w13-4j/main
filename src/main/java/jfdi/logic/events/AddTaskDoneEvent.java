// @@author A0130195M

package jfdi.logic.events;

import jfdi.storage.apis.TaskAttributes;

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
