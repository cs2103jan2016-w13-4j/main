package jfdi.ui.items;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import jfdi.storage.apis.TaskAttributes;
import jfdi.ui.Constants;

public class ListItem extends VBox {

    @FXML
    private Label rowIndex;
    @FXML
    private Label description;
    @FXML
    private Label timeAndDate;

    private int index;
    private TaskAttributes item;
    private Boolean mark;

    public ListItem(int index, TaskAttributes task, boolean bln) {
        loadView();
        setIndex(index);
        setItem(task);
        if (bln) {
            setMarkT();
        } else {
            setMarkF();
        }
    }

    private void loadView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Constants.URL_ITEM_PATH));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setIndex(int index) {
        this.index = index;
        rowIndex.setText(String.format(Constants.ITEM_ROW_INDEX, this.index));
    }

    public int getIndex() {
        return this.index;
    }

    public void setItem(TaskAttributes task) {
        this.item = task;
        setDescription(item.getDescription());
        setTimeDate(task.getStartDateTime(), task.getEndDateTime());
    }

    public TaskAttributes getItem() {
        return this.item;
    }

    public void setDescription(String string) {
        description.setWrapText(true);
        description.setText(string);
    }

    public void setTimeDate(LocalDateTime startTime, LocalDateTime endTime) {
        timeAndDate.setWrapText(true);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM yyyy h:mma");
        if (startTime == null) {
            if (endTime == null) {
                // Floating Tasks
                timeAndDate.setText(Constants.ITEM_NO_TIMEDATE);
            } else {
                // Deadline Tasks
                String end = formatter.format(endTime);
                timeAndDate.setText(String.format(Constants.ITEM_DEADLINE, end));
            }
        } else {
            String start = formatter.format(startTime);
            if (endTime == null) {
                // Point Tasks
                timeAndDate.setText(String.format(Constants.ITEM_POINT_TASK, start));
            } else {
                // Event Tasks
                String end = formatter.format(endTime);
                timeAndDate.setText(String.format(Constants.ITEM_EVENT_TASK, start, end));
            }
        }
    }

    public void setMarkT() {
        this.mark = true;
        //  NEED TO BE ABLE TO STRIKE OUT THE TASK
    }

    public void setMarkF() {
        this.mark = false;
        // UNSTRIKE IF IT IS PREVIOUSLY STRIKED
    }

    public Boolean getMark() {
        return this.mark;
    }

    public void strikeOut() {
        this.getStyleClass().setAll("itemBoxDone");
    }

    public void removeStrike() {
        this.getStyleClass().setAll("itemBox");
    }
}
