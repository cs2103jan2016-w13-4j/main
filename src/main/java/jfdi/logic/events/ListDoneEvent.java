package jfdi.logic.events;

import jfdi.storage.apis.TaskAttributes;

import java.util.ArrayList;

/**
 * @author Liu Xinan
 */
public class ListDoneEvent {

    private ArrayList<String> tags;
    private ArrayList<TaskAttributes> items;

    public ListDoneEvent(ArrayList<String> tags, ArrayList<TaskAttributes> items) {
        this.tags = tags;
        this.items = items;
    }

    public ArrayList<TaskAttributes> getItems() {
        return items;
    }

    public ArrayList<String> getTags() {
        return tags;
    }
}
