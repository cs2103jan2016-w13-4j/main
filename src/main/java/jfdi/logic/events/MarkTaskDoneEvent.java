// @@author A0130195M
package jfdi.logic.events;

import jfdi.storage.apis.TaskAttributes;

import java.util.ArrayList;

/**
 * @author Liu Xinan
 */
public class MarkTaskDoneEvent {

    private ArrayList<Integer> screenIds;
    private ArrayList<TaskAttributes> markedTasks;

    public MarkTaskDoneEvent(ArrayList<Integer> screenIds, ArrayList<TaskAttributes> markedTasks) {
        this.screenIds = screenIds;
        this.markedTasks = markedTasks;
    }

    public ArrayList<Integer> getScreenIds() {
        return screenIds;
    }

    public ArrayList<TaskAttributes> getMarkedTasks() {
        return markedTasks;
    }

}
