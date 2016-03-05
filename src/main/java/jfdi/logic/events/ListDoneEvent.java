package jfdi.logic.events;

import jfdi.storage.data.TaskAttributes;

import java.util.ArrayList;

/**
 * @author Liu Xinan
 */
public class ListDoneEvent {

    private ArrayList<TaskAttributes> items;

    public ListDoneEvent(ArrayList<TaskAttributes> items) {
        this.items = items;
    }

    public ArrayList<TaskAttributes> getItems() {
        return items;
    }

}
