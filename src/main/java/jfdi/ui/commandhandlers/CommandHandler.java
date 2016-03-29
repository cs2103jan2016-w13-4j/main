package jfdi.ui.commandhandlers;

import java.util.ArrayList;
import java.util.logging.Logger;

import edu.emory.mathcs.backport.java.util.Collections;
import jfdi.common.utilities.JfdiLogger;
import jfdi.logic.events.ListDoneEvent;
import jfdi.storage.apis.TaskAttributes;
import jfdi.ui.Constants.ListStatus;
import jfdi.ui.MainController;
import jfdi.ui.items.ListItem;

public abstract class CommandHandler {

    public MainController controller;
    public Logger logger = JfdiLogger.getLogger();

    private void refreshDisplay() {
        controller.listMain.refresh();
    }

    public void setController(MainController controller) {
        this.controller = controller;
    }

    /**
     * Sets the display list to the given ArrayList of tasks which match the
     * context.
     *
     * @param tasks
     *            the ArrayList of tasks to be displayed
     */
    private void listTasks(ArrayList<TaskAttributes> tasks,
        boolean shouldCheckContext) {
        controller.importantList.clear();
        for (TaskAttributes task : tasks) {
            appendTaskToDisplayList(task, shouldCheckContext);
        }
    }

    /**
     * Appends a task to the list of tasks displayed.
     *
     * @param task
     *            the task to be appended
     */
    private void appendTaskToDisplayList(TaskAttributes task,
        boolean shouldCheckContext) {
        if (shouldCheckContext && !isSameContext(task)) {
            return;
        }

        int onScreenId = controller.importantList.size() + 1;
        ListItem listItem;

        if (task.isCompleted()) {
            listItem = new ListItem(onScreenId, task, true);
            controller.importantList.add(listItem);
            controller.importantList.get(controller.importantList.size() - 1)
                .strikeOut();
            listItem.strikeOut();
        } else {
            listItem = new ListItem(onScreenId, task, false);
            controller.importantList.add(listItem);
            controller.importantList.get(controller.importantList.size() - 1)
                .getStyleClass().add("itemBox");
        }
    }

    private void sortDisplayList() {
        ArrayList<TaskAttributes> taskList = new ArrayList<TaskAttributes>();
        controller.importantList.forEach(listItem -> taskList.add(listItem
            .getItem()));
        Collections.sort(taskList);
        listTasks(taskList, false);
    }

    private boolean shouldSort() {
        return controller.displayStatus.equals(ListStatus.OVERDUE)
            || controller.displayStatus.equals(ListStatus.UPCOMING);
    }

    private boolean isSameContext(TaskAttributes task) {
        switch (controller.displayStatus) {
            case ALL:
                return true;
            case SEARCH:
                return false;
            case SURPRISE:
                return false;
            case HELP:
                return false;
            case COMPLETE:
                return task.isCompleted();
            case INCOMPLETE:
                return !task.isCompleted();
            case OVERDUE:
                return task.isOverdue();
            case UPCOMING:
                return task.isUpcoming();
            default:
                assert false;
                return false;
        }
    }

    private void switchContext(ListStatus status, Boolean isListing) {
        if (status.equals(ListStatus.HELP)) {
            controller.beforeHelp = controller.displayStatus;
        }
        controller.displayStatus = status;
        if (isListing) {
            controller.transListCmd();
        }
        controller.switchTabSkin();
    }

    private void updateBubble(ListDoneEvent e) {
        Integer count = e.getItems().size();
        switch (e.getListType()) {
            case INCOMPLETE:
                controller.incompletePlaceHdr.set(count.toString());
                break;
            case OVERDUE:
                controller.overduePlaceHdr.set(count.toString());
                break;
            case UPCOMING:
                controller.upcomingPlaceHdr.set(count.toString());
                break;
            default:
                break;
        }
    }
}
