package jfdi.logic.events;

/**
 * @author Liu Xinan
 */
public class AddTaskFailedEvent {

    public enum Error {
        EMPTY_DESCRIPTION, UNKNOWN
    }

    private Error error;

    public AddTaskFailedEvent(Error error) {
        this.error = error;
    }

    public Error getError() {
        return error;
    }
}
