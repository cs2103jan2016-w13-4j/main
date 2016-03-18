package jfdi.ui;

import java.time.format.DateTimeFormatter;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import jfdi.storage.apis.TaskAttributes;

public class ListItem extends HBox {

    @FXML
    private Label rowIndex;
    @FXML
    private Label description;
    @FXML
    private Label timeAndDate;

    private int index;
    private TaskAttributes item;
    private final BooleanProperty on = new SimpleBooleanProperty();

    public ListItem(int index, TaskAttributes task, boolean on) {
        loadView();
        setIndex(index);
        setItem(task);
        setOn(on);
    }

    private void loadView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Constants.URL_ITEM_PATH));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();

        } catch (Exception e) {
            e.printStackTrace();
        }    }

    public void setIndex(int index) {
        this.index = index;
        rowIndex.setText(String.format(Constants.ITEM_ROW_INDEX, this.index));
    }

    public int getIndex() {
        return this.index;
    }

    public final void setItem(TaskAttributes task) {
        this.item = task;
        description.setText(item.getDescription());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM yyyy h:mma");

        if (item.getStartDateTime() == null && item.getEndDateTime() == null) {
            // Floating Tasks
            timeAndDate.setText(Constants.ITEM_NO_TIMEDATE);
        } else if (item.getStartDateTime() == null && item.getEndDateTime() != null) {
            // Deadline Tasks
            timeAndDate.setText(String.format(Constants.ITEM_DEADLINE,
                    formatter.format(item.getEndDateTime())));
        } else if (item.getStartDateTime() != null && item.getEndDateTime() == null) {
            // Point Tasks
            timeAndDate.setText(String.format(Constants.ITEM_POINT_TASK,
                    formatter.format(item.getStartDateTime())));
        } else if (item.getStartDateTime() != null && item.getEndDateTime() != null) {
            // Event Tasks
            timeAndDate.setText(String.format(Constants.ITEM_EVENT_TASK,
                    formatter.format(item.getStartDateTime()),
                    formatter.format(item.getEndDateTime())));
        }
    }

    public TaskAttributes getItem() {
        return this.item;
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
}
