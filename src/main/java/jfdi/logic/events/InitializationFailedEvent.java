package jfdi.logic.events;

/**
 * @author Xinan
 */
public class InitializationFailedEvent {

    public enum Error {
        INVALID_PATH, UNKNOWN
    }

    private Error error;
    private String path;

    public InitializationFailedEvent(Error error, String path) {
        this.error = error;
        this.path = path;
    }

    public Error getError() {
        return error;
    }

    public String getPath() {
        return path;
    }

}
