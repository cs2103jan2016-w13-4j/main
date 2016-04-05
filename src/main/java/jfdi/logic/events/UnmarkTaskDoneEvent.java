package jfdi.logic.events;

import jfdi.storage.apis.TaskAttributes;

import java.util.ArrayList;

/**
 * @author Liu Xinan
 */
public class UnmarkTaskDoneEvent {

    private ArrayList<Integer> screenIds;
    private ArrayList<TaskAttributes> unmarkedTasks;

    public UnmarkTaskDoneEvent(ArrayList<Integer> screenIds, ArrayList<TaskAttributes> unmarkedTasks) {
        this.screenIds = screenIds;
        this.unmarkedTasks = unmarkedTasks;
    }

    public ArrayList<Integer> getScreenIds() {
        return screenIds;
    }

    public ArrayList<TaskAttributes> getUnmarkedTasks() {
        return unmarkedTasks;
    }

}
