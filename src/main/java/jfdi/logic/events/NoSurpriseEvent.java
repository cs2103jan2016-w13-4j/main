package jfdi.logic.events;

/**
 * @author Xinan
 */
public class NoSurpriseEvent {

    public enum Error {
        NO_TASKS, UNKNOWN
    }

    private Error error;

    public NoSurpriseEvent(Error error) {
        this.error = error;
    }

    public Error getError() {
        return error;
    }

}
