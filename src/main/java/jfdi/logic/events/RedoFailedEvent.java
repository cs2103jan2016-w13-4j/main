package jfdi.logic.events;

/**
 * @author Liu Xinan
 */
public class RedoFailedEvent {

    public enum Error {
        NONTHING_TO_REDO, UNKNOWN
    }

    private Error error;

    public RedoFailedEvent(Error error) {

        this.error = error;
    }

    public Error getError() {
        return error;
    }

}
