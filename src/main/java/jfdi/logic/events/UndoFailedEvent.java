package jfdi.logic.events;

/**
 * @author Liu Xinan
 */
public class UndoFailedEvent {

    public enum Error {
        NONTHING_TO_UNDO, UNKNOWN
    }

    private Error error;

    public UndoFailedEvent(Error error) {
        this.error = error;
    }

    public Error getError() {
        return error;
    }

}
