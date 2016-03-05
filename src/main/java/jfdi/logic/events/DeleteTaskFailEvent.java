package jfdi.logic.events;

import java.util.ArrayList;

/**
 * @author Liu Xinan
 */
public class DeleteTaskFailEvent {

    public enum Error {
        NON_EXISTENT_ID, UNKNOWN
    }

    private ArrayList<Integer> invalidIds;
    private Error error;

    public DeleteTaskFailEvent() {
        error = Error.UNKNOWN;
    }

    public DeleteTaskFailEvent(ArrayList<Integer> invalidIds) {
        error = Error.NON_EXISTENT_ID;
        this.invalidIds = invalidIds;
    }

    public ArrayList<Integer> getInvalidIds() {
        return invalidIds;
    }

    public Error getError() {
        return error;
    }
}
