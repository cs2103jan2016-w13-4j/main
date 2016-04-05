package jfdi.logic.events;

import jfdi.storage.apis.TaskAttributes;

/**
 * @@author Liu Xinan
 */
public class SurpriseEvent {

    private TaskAttributes task;

    public SurpriseEvent(TaskAttributes task) {
        this.task = task;
    }

    public TaskAttributes getTask() {
        return task;
    }

}
