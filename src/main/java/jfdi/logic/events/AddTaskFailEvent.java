package jfdi.logic.events;

/**
 * @author Liu Xinan
 */
public class AddTaskFailEvent {

    public enum Error {
        EMPTY_DESCRIPTION, UNKNOWN
    }

    private Error error;

    public AddTaskFailEvent(Error error) {
        this.error = error;
    }

    public Error getError() {
        return error;
    }
}
