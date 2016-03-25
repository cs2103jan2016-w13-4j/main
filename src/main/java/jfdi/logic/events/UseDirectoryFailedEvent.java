package jfdi.logic.events;

/**
 * @author Xinan
 */
public class UseDirectoryFailedEvent {

    public enum Error {
        INVALID_PATH, UNKNOWN
    }

    private String newDirectory;
    private Error error;

    public UseDirectoryFailedEvent(String newDirectory, Error error) {
        this.newDirectory = newDirectory;
        this.error = error;
    }

    public String getNewDirectory() {
        return newDirectory;
    }

    public Error getError() {
        return error;
    }

}
