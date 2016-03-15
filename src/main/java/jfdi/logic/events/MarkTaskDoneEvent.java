package jfdi.logic.events;

import jfdi.storage.apis.TaskAttributes;

import java.util.ArrayList;

/**
 * @author Liu Xinan
 */
public class MarkTaskDoneEvent {

    private ArrayList<Integer> taskIds;
    private ArrayList<TaskAttributes> markedTasks;

    public MarkTaskDoneEvent(ArrayList<Integer> taskIds, ArrayList<TaskAttributes> markedTasks) {
        this.taskIds = taskIds;
        this.markedTasks = markedTasks;
    }

    public ArrayList<Integer> getTaskIds() {
        return taskIds;
    }

    public ArrayList<TaskAttributes> getMarkedTasks() {
        return markedTasks;
    }

}
