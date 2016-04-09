// @@author A0130195M

package jfdi.logic.events;

/**
 * @author Liu Xinan
 */
public class RenameTaskFailedEvent {

    public enum Error {
        NON_EXISTENT_ID,
        DUPLICATED_TASK,
        NO_CHANGES,
        UNKNOWN
    }

    private int screenId;
    private String description;
    private Error error;

    public RenameTaskFailedEvent(int screenId, String description, Error error) {
        this.screenId = screenId;
        this.description = description;
        this.error = error;
    }

    public int getScreenId() {
        return screenId;
    }

    public String getDescription() {
        return description;
    }

    public Error getError() {
        return error;
    }
}
