package jfdi.logic.events;

import java.util.ArrayList;

/**
 * @@author Liu Xinan
 */
public class UnmarkTaskFailedEvent {

    public enum Error {
        NON_EXISTENT_ID, UNKNOWN
    }

    private ArrayList<Integer> screenIds;
    private ArrayList<Integer> invalidIds;
    private Error error;

    public UnmarkTaskFailedEvent(ArrayList<Integer> screenIds, ArrayList<Integer> invalidIds) {
        this.screenIds = screenIds;
        this.invalidIds = invalidIds;
        this.error = Error.NON_EXISTENT_ID;
    }

    public ArrayList<Integer> getScreenIds() {
        return screenIds;
    }

    public ArrayList<Integer> getInvalidIds() {
        return invalidIds;
    }

    public Error getError() {
        return error;
    }

}
