package jfdi.logic.events;

import java.time.LocalDateTime;

/**
 * @author Liu Xinan
 */
public class RescheduleTaskDoneEvent {

    private int taskId;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    public RescheduleTaskDoneEvent(int taskId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.taskId = taskId;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    public int getTaskId() {
        return taskId;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }
}
