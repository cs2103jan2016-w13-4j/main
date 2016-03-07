package jfdi.logic.events;

import java.time.LocalDateTime;

/**
 * @author Liu Xinan
 */
public class RescheduleTaskFailEvent {

    public enum Error {
        NON_EXISTENT_ID,
        NO_CHANGES,
        UNKNOWN
    }

    private int taskId;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private Error error;

    public RescheduleTaskFailEvent(int taskId, LocalDateTime startDateTime, LocalDateTime endDateTime, Error error) {
        this.taskId = taskId;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.error = error;
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

    public Error getError() {
        return error;
    }
}
