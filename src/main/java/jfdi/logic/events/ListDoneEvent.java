package jfdi.logic.events;

import jfdi.storage.apis.TaskAttributes;
import jfdi.logic.commands.ListCommand.ListType;

import java.util.ArrayList;

/**
 * @author Liu Xinan
 */
public class ListDoneEvent {

    private ListType listType;
    private ArrayList<TaskAttributes> items;

    public ListDoneEvent(ListType listType, ArrayList<TaskAttributes> items) {
        this.listType = listType;
        this.items = items;
    }

    public ArrayList<TaskAttributes> getItems() {
        return items;
    }

    public ListType getListType() {
        return listType;
    }
}
