// @@author A0130195M
package jfdi.logic.events;

import java.util.ArrayList;

/**
 * @author Liu Xinan
 */
public class MarkTaskFailedEvent {

    public enum Error {
        NON_EXISTENT_ID
    }

    private ArrayList<Integer> screenIds;
    private ArrayList<Integer> invalidIds;
    private Error error;

    public MarkTaskFailedEvent(ArrayList<Integer> screenIds, ArrayList<Integer> invalidIds) {
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
