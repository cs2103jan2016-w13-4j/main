package jfdi.logic.events;

/**
 * @author Liu Xinan
 */
public class ListFailEvent {

    public enum Error {
        NON_EXISTENT_TAG, UNKNOWN
    }

    private Error error;

    public ListFailEvent(Error error) {
        this.error = error;
    }

    public Error getError() {
        return error;
    }

}
