// @@author A0130195M
package jfdi.logic.events;

import java.time.LocalDateTime;

/**
 * @author Liu Xinan
 */
public class RescheduleTaskFailedEvent {

    public enum Error {
        NON_EXISTENT_ID,
        DUPLICATED_TASK,
        NO_CHANGES,
        UNKNOWN
    }

    private int screenId;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private Error error;

    public RescheduleTaskFailedEvent(int screenId, LocalDateTime startDateTime, LocalDateTime endDateTime,
                                     Error error) {
        this.screenId = screenId;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.error = error;
    }

    public int getScreenId() {
        return screenId;
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
