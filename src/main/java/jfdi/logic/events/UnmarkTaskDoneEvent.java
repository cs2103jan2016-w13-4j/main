package jfdi.logic.events;

import jfdi.storage.apis.TaskAttributes;

import java.util.ArrayList;

/**
 * @author Liu Xinan
 */
public class UnmarkTaskDoneEvent {

    private ArrayList<Integer> taskIds;
    private ArrayList<TaskAttributes> unmarkedTasks;

    public UnmarkTaskDoneEvent(ArrayList<Integer> taskIds, ArrayList<TaskAttributes> unmarkedTasks) {
        this.taskIds = taskIds;
        this.unmarkedTasks = unmarkedTasks;
    }

    public ArrayList<Integer> getTaskIds() {
        return taskIds;
    }

    public ArrayList<TaskAttributes> getUnarkedTasks() {
        return unmarkedTasks;
    }

}
