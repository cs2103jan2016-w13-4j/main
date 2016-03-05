package jfdi.logic.events;

/**
 * @author Liu Xinan
 */
public class AddTaskFailEvent {

    public enum Error {
        UNKNOWN
    }

    private Error error;

    public AddTaskFailEvent(Error error) {
        this.error = error;
    }

    public Error getError() {
        return error;
    }
}
