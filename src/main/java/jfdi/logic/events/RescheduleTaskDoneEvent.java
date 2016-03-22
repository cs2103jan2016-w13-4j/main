package jfdi.logic.events;

import jfdi.storage.apis.TaskAttributes;

import java.time.LocalDateTime;

/**
 * @author Liu Xinan
 */
public class RescheduleTaskDoneEvent {

    private TaskAttributes task;

    public RescheduleTaskDoneEvent(TaskAttributes task) {
        this.task = task;
    }

    public TaskAttributes getTask() {
        return task;
    }

}
