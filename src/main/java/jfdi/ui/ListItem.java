package jfdi.ui;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import jfdi.storage.apis.TaskAttributes;

public class ListItem {

    private int index;
    private TaskAttributes item;
    private final BooleanProperty on = new SimpleBooleanProperty();

    public ListItem(int index, TaskAttributes task, boolean on) {
        setIndex(index);
        setTask(task);
        setOn(on);
    }

    public int getIndex() {
        return this.index;
    }

    public TaskAttributes getItem() {
        return this.item;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public final void setTask(TaskAttributes task) {
        this.item = task;
    }

    public final BooleanProperty onProperty() {
        return this.on;
    }

    public final boolean isOn() {
        return this.onProperty().get();
    }

    public final void setOn(final boolean on) {
        this.onProperty().set(on);
    }

    public void decIndex() {
        index--;
    }

    public void incIndex() {
        index++;
    }

    @Override
    public String toString() {
        String display = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM yyyy h:mma");

        if (item.getStartDateTime() == null && item.getEndDateTime() == null) {
            // Floating Tasks
            display = " # " + index + " :  " + item.getDescription();
        } else if (item.getStartDateTime() == null && item.getEndDateTime() != null) {
            // Deadline Tasks
            display = " # " + index + " :  " + item.getDescription() + " by "
                    + formatter.format(item.getEndDateTime());
        } else if (item.getStartDateTime() != null && item.getEndDateTime() == null) {
            // Point Tasks
            display = " # " + index + " :  " + item.getDescription() + " at "
                    + formatter.format(item.getStartDateTime());
        } else if (item.getStartDateTime() != null && item.getEndDateTime() != null) {
            // Event Tasks
            display = " # " + index + " :  " + item.getDescription() + " from "
                    + formatter.format(item.getStartDateTime()) + " to " + formatter.format(item.getEndDateTime());
        }

        return display;
    }

}
