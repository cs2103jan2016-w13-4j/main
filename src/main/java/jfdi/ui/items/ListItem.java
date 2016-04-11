// @@author A0129538W

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
    @FXML
    private Label headerName;

    private String name;
    private int index;
    private TaskAttributes item;
    private Boolean mark;
    private Boolean isHeader = false;

    /**
     * Constructor for task list items
     *
     * @param index
     *            the on-screen index
     * @param task
     *            the task to be displayed
     * @param isCompleted
     *            flag for indicating the incomplete/completed
     *
     */
    public ListItem(int index, TaskAttributes task, boolean isCompleted) {
        loadView();
        setIndex(index);
        setItem(task);
        if (isCompleted) {
            setMarkT();
        } else {
            setMarkF();
        }
    }

    /**
     * Constructor for header list items
     *
     * @param name
     *            name of the header
     */
    public ListItem(String name) {
        loadHeaderView();
        setName(name);
        isHeader = true;
    }

    /**
     * Loads the view and controller of the ListItem fxml files.
     */
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

    /**
     * Loads the view and controller of the ListHeader fxml files.
     */
    private void loadHeaderView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Constants.URL_HEADER_PATH));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the name of the item. (For list headers)
     *
     * @param name
     *            name of the item
     */
    private void setName(String name) {
        this.name = name;
        headerName.setText(this.name);
    }

    /**
     * Sets the on-screen index of the item. (For list items)
     *
     * @param index
     *            on-screen index of the item
     */
    public void setIndex(int index) {
        this.index = index;
        rowIndex.setText(String.format(Constants.ITEM_ROW_INDEX, this.index));
    }

    /**
     * Sets the task object to be displayed. (For list items)
     *
     * @param task
     *            task to be displayed
     */
    public void setItem(TaskAttributes task) {
        this.item = task;
        setDescription(item.getDescription());
        setTimeDate(task.getStartDateTime(), task.getEndDateTime());
    }

    /**
     * Sets the description of the item. (For list items)
     *
     * @param string
     *            description of the task (cannot be null)
     */
    public void setDescription(String string) {
        description.setWrapText(true);
        description.setText(string);
    }

    /**
     * Sets the date and time of the item. (For list items)
     *
     * @param startTime
     *            start time of the task (can be null)
     * @param endTime
     *            end time of the task (can be null)
     */
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

    /**
     * Sets the task as incomplete. (For list items)
     */
    public void setMarkT() {
        this.setMark(true);
    }

    /**
     * Sets the task as completed. (For list items)
     */
    public void setMarkF() {
        this.setMark(false);
    }

    /**
     * Set the completed status for the task. (For list items)
     *
     * @param mark
     *            flag for representing completed status
     */
    private void setMark(Boolean mark) {
        this.mark = mark;
    }

    /**
     * Sets the task as stroke-out on display. (For list items)
     */
    public void strikeOut() {
        this.getStyleClass().setAll("itemBoxDone");
    }

    /**
     * Remove the strike-out on the task on display if previously stroked. (For
     * list items)
     */
    public void removeStrike() {
        this.getStyleClass().setAll("itemBox");
    }

    /**
     * Get Methods for private variables
     */
    public boolean getIsHeader() {
        return isHeader;
    }

    public int getIndex() {
        return this.index;
    }

    public String getDescription() {
        return description.getText();
    }

    public TaskAttributes getItem() {
        return this.item;
    }

    public Boolean getMark() {
        return mark;
    }
}
