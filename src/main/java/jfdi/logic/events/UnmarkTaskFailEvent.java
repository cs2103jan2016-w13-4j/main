package jfdi.logic.events;

import java.util.ArrayList;

/**
 * @author Liu Xinan
 */
public class UnmarkTaskFailEvent {

    public enum Error {
        NON_EXISTENT_ID, UNKNOWN
    }

    private ArrayList<Integer> taskIds;
    private ArrayList<Integer> invalidIds;
    private Error error;

    public UnmarkTaskFailEvent(ArrayList<Integer> taskIds, ArrayList<Integer> invalidIds) {
        this.taskIds = taskIds;
        this.invalidIds = invalidIds;
        this.error = Error.NON_EXISTENT_ID;
    }

    public ArrayList<Integer> getTaskIds() {
        return taskIds;
    }

    public ArrayList<Integer> getInvalidIds() {
        return invalidIds;
    }

    public Error getError() {
        return error;
    }

}
