// @@author A0130195M

package jfdi.logic.events;

/**
 * @author Liu Xinan
 */
public class AddTaskFailedEvent {

    public enum Error {
        EMPTY_DESCRIPTION, DUPLICATED_TASK
    }

    private Error error;

    public AddTaskFailedEvent(Error error) {
        this.error = error;
    }

    public Error getError() {
        return error;
    }
}
