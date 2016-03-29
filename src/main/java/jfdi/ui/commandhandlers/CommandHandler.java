package jfdi.ui.commandhandlers;

import java.util.ArrayList;
import java.util.logging.Logger;

import edu.emory.mathcs.backport.java.util.Collections;
import jfdi.common.utilities.JfdiLogger;
import jfdi.logic.events.ListDoneEvent;
import jfdi.storage.apis.TaskAttributes;
import jfdi.ui.Constants.ListStatus;
import jfdi.ui.MainController;
import jfdi.ui.UI;
import jfdi.ui.items.ListItem;

public abstract class CommandHandler {

    public MainController controller = UI.getInstance().controller;
    public static Logger logger = JfdiLogger.getLogger();

    protected void refreshDisplay() {
        controller.listMain.refresh();
    }

    public void setController(MainController ctrl) {
        controller = ctrl;
    }
    
    public static void registerEvents() {
        UI.getEventBus().register(AddHandler.getInstance());
        UI.getEventBus().register(AliasHandler.getInstance());
        UI.getEventBus().register(DeleteHandler.getInstance());
        UI.getEventBus().register(ExitHandler.getInstance());
        UI.getEventBus().register(HelpHandler.getInstance());
        UI.getEventBus().register(InitializationHandler.getInstance());
        UI.getEventBus().register(InvalidCmdHandler.getInstance());
        UI.getEventBus().register(ListHandler.getInstance());
        UI.getEventBus().register(MarkHandler.getInstance());
        UI.getEventBus().register(MoveDirHandler.getInstance());
        UI.getEventBus().register(RedoHandler.getInstance());
        UI.getEventBus().register(RenameHandler.getInstance());
        UI.getEventBus().register(RescheduleHandler.getInstance());
        UI.getEventBus().register(SearchHandler.getInstance());
        UI.getEventBus().register(ShowDirHandler.getInstance());
        UI.getEventBus().register(SurpriseHandler.getInstance());
        UI.getEventBus().register(UnaliasHandler.getInstance());
        UI.getEventBus().register(UndoHandler.getInstance());
        UI.getEventBus().register(UnmarkHandler.getInstance());
        UI.getEventBus().register(UseDirHandler.getInstance());
    }

    /**
     * Sets the display list to the given ArrayList of tasks which match the
     * context.
     *
     * @param tasks
     *            the ArrayList of tasks to be displayed
     */
    protected void listTasks(ArrayList<TaskAttributes> tasks,
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
    protected void appendTaskToDisplayList(TaskAttributes task,
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

    protected void sortDisplayList() {
        ArrayList<TaskAttributes> taskList = new ArrayList<TaskAttributes>();
        controller.importantList.forEach(listItem -> taskList.add(listItem
            .getItem()));
        Collections.sort(taskList);
        listTasks(taskList, false);
    }

    protected boolean shouldSort() {
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

    protected void switchContext(ListStatus status, Boolean isListing) {
        if (status.equals(ListStatus.HELP)) {
            controller.beforeHelp = controller.displayStatus;
        }
        controller.displayStatus = status;
        if (isListing) {
            controller.transListCmd();
        }
        controller.switchTabSkin();
    }

    protected void updateBubble(ListDoneEvent e) {
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
