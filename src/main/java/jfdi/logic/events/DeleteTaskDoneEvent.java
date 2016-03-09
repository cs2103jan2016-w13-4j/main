package jfdi.logic.events;

import jfdi.storage.apis.TaskAttributes;

import java.util.ArrayList;

/**
 * @author Liu Xinan
 */
public class DeleteTaskDoneEvent {

    private ArrayList<Integer> deletedIds;
    private ArrayList<TaskAttributes> deletedTasks;

    public DeleteTaskDoneEvent(ArrayList<Integer> deletedIds, ArrayList<TaskAttributes> deletedTasks) {
        this.deletedIds = deletedIds;
    }

    public ArrayList<Integer> getDeletedIds() {
        return deletedIds;
    }

    public ArrayList<TaskAttributes> getDeletedTasks() {
        return deletedTasks;
    }
}
