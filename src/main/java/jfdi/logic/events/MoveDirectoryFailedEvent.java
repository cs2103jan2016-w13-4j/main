package jfdi.logic.events;

/**
 * @author Liu Xinan
 */
public class MoveDirectoryFailedEvent {

    public enum Error {
        INVALID_PATH, UNKNOWN
    }

    private String newDirectory;
    private Error error;

    public MoveDirectoryFailedEvent(String newDirectory, Error error) {
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
