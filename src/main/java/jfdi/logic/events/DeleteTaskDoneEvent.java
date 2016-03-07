package jfdi.logic.events;

import java.util.ArrayList;

/**
 * @author Liu Xinan
 */
public class DeleteTaskDoneEvent {

    private ArrayList<Integer> deletedIds;

    public DeleteTaskDoneEvent(ArrayList<Integer> deletedIds) {
        this.deletedIds = deletedIds;
    }

    public ArrayList<Integer> getDeletedIds() {
        return deletedIds;
    }
}
