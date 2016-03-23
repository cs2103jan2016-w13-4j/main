package jfdi.ui.commandhandlers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.logging.Logger;

import jfdi.common.utilities.JfdiLogger;
import jfdi.logic.events.AddTaskDoneEvent;
import jfdi.logic.events.AddTaskFailedEvent;
import jfdi.logic.events.AliasDoneEvent;
import jfdi.logic.events.AliasFailedEvent;
import jfdi.logic.events.CommandRedoneEvent;
import jfdi.logic.events.CommandUndoneEvent;
import jfdi.logic.events.DeleteTaskDoneEvent;
import jfdi.logic.events.DeleteTaskFailedEvent;
import jfdi.logic.events.ExitCalledEvent;
import jfdi.logic.events.HelpRequestedEvent;
import jfdi.logic.events.InitializationFailedEvent;
import jfdi.logic.events.InvalidCommandEvent;
import jfdi.logic.events.ListDoneEvent;
import jfdi.logic.events.MarkTaskDoneEvent;
import jfdi.logic.events.MarkTaskFailedEvent;
import jfdi.logic.events.MoveDirectoryDoneEvent;
import jfdi.logic.events.MoveDirectoryFailedEvent;
import jfdi.logic.events.NoSurpriseEvent;
import jfdi.logic.events.RedoFailedEvent;
import jfdi.logic.events.RenameTaskDoneEvent;
import jfdi.logic.events.RenameTaskFailedEvent;
import jfdi.logic.events.RescheduleTaskDoneEvent;
import jfdi.logic.events.RescheduleTaskFailedEvent;
import jfdi.logic.events.SearchDoneEvent;
import jfdi.logic.events.ShowDirectoryEvent;
import jfdi.logic.events.SurpriseEvent;
import jfdi.logic.events.UnaliasDoneEvent;
import jfdi.logic.events.UnaliasFailEvent;
import jfdi.logic.events.UndoFailedEvent;
import jfdi.logic.events.UnmarkTaskDoneEvent;
import jfdi.logic.events.UnmarkTaskFailEvent;
import jfdi.logic.events.UseDirectoryDoneEvent;
import jfdi.logic.events.UseDirectoryFailedEvent;
import jfdi.logic.interfaces.Command;
import jfdi.storage.apis.TaskAttributes;
import jfdi.storage.exceptions.FilePathPair;
import jfdi.ui.Constants;
import jfdi.ui.Constants.MsgType;
import jfdi.ui.MainController;
import jfdi.ui.items.ListItem;

import com.google.common.eventbus.Subscribe;

import edu.emory.mathcs.backport.java.util.Collections;

public class CommandHandler {

    public MainController controller;
    public Logger logger = JfdiLogger.getLogger();

    @Subscribe
    public void handleAddTaskDoneEvent(AddTaskDoneEvent e) {
        TaskAttributes task = e.getTask();
        int index = appendTaskToDisplayList(task);
        controller.relayFb(
            String.format(Constants.CMD_SUCCESS_ADDED, index,
                task.getDescription()), MsgType.SUCCESS);
        logger.fine(String.format(Constants.LOG_ADDED_SUCCESS, task.getId()));
    }

    @Subscribe
    public void handleAddTaskFailEvent(AddTaskFailedEvent e) {
        switch (e.getError()) {
            case UNKNOWN:
                controller.relayFb(Constants.CMD_ERROR_CANT_ADD_UNKNOWN,
                    MsgType.ERROR);
                logger.fine(String.format(Constants.LOG_ADD_FAIL_UNKNOWN));
                break;
            case EMPTY_DESCRIPTION:
                controller.relayFb(Constants.CMD_ERROR_CANT_ADD_EMPTY,
                    MsgType.ERROR);
                logger.fine(String.format(Constants.LOG_ADD_FAIL_EMPTY));
                break;
            default:
                break;
        }
    }

    @Subscribe
    public void handleAliasDoneEvent(AliasDoneEvent e) {
        controller.relayFb(
            String.format(Constants.CMD_SUCCESS_ALIAS, e.getAlias(),
                e.getCommand()), MsgType.SUCCESS);
    }

    @Subscribe
    public void handleAliasFailEvent(AliasFailedEvent e) {
        switch (e.getError()) {
            case INVALID_PARAMETERS:
                controller.relayFb(
                    String.format(Constants.CMD_ERROR_CANT_ALIAS_INVALID,
                        e.getAlias(), e.getCommand()), MsgType.ERROR);
                // logger.fine(String.format(format, args));
                break;
            case DUPLICATED_ALIAS:
                controller.relayFb(
                    String.format(Constants.CMD_ERROR_CANT_ALIAS_DUPLICATED,
                        e.getAlias()), MsgType.ERROR);
                // logger.fine(String.format(format, args));
                break;
            case UNKNOWN:
                controller.relayFb(
                    String.format(Constants.CMD_ERROR_CANT_ALIAS_UNKNOWN,
                        e.getCommand()), MsgType.ERROR);
                // logger.fine(String.format(format, args));
                break;
            default:
                break;
        }
    }

    @Subscribe
    public void handleCommandRedoneEvent(CommandRedoneEvent e) {
        Class<? extends Command> cmdType = e.getCommandType();
        controller.displayList(controller.displayStatus);
        controller.relayFb(
            String.format(Constants.CMD_SUCCESS_REDONE, cmdType.toString()),
            MsgType.SUCCESS);
    }

    @Subscribe
    public void handleCommandUndoneEvent(CommandUndoneEvent e) {
        Class<? extends Command> cmdType = e.getCommandType();
        controller.displayList(controller.displayStatus);
        controller.relayFb(
            String.format(Constants.CMD_SUCCESS_UNDONE, cmdType.toString()),
            MsgType.SUCCESS);
    }

    @Subscribe
    public void handleDeleteTaskDoneEvent(DeleteTaskDoneEvent e) {
        ArrayList<Integer> deletedIds = e.getDeletedIds();
        Collections.sort(deletedIds, Comparator.reverseOrder());

        int indexCount = -1;
        for (int screenId : deletedIds) {
            indexCount = screenId - 1;
            controller.importantList.remove(indexCount);
            logger.fine(String.format(Constants.LOG_DELETED_SUCCESS, screenId));
        }
        controller.relayFb(Constants.CMD_SUCCESS_DELETED, MsgType.SUCCESS);

        indexCount = 1;
        for (ListItem item : controller.importantList) {
            if (item.getIndex() != indexCount) {
                item.setIndex(indexCount);
            }
            indexCount++;
        }
    }

    @Subscribe
    public void handleDeleteTaskFailEvent(DeleteTaskFailedEvent e) {
        switch (e.getError()) {
            case UNKNOWN:
                controller.relayFb(Constants.CMD_ERROR_CANT_DELETE_UNKNOWN,
                    MsgType.ERROR);
                logger.fine(Constants.LOG_DELETE_FAIL_UNKNOWN);
                break;
            case NON_EXISTENT_ID:
                for (Integer screenId : e.getInvalidIds()) {
                    controller.relayFb(String.format(
                        Constants.CMD_ERROR_CANT_DELETE_NO_ID, screenId),
                        MsgType.ERROR);
                }
                logger.fine(Constants.LOG_DELETE_FAIL_NOID);
                break;
            default:
                break;
        }
    }

    @Subscribe
    public void handleExitCalledEvent(ExitCalledEvent e) {
        System.out.printf("\nMoriturus te saluto.\n");
        System.exit(0);
        logger.fine(Constants.LOG_USER_EXIT);
    }

    @Subscribe
    public void handleHelpRequestEvent(HelpRequestedEvent e) {
        controller.showHelpDisplay();
    }

    @Subscribe
    public void handleInitializationFailedEvent(InitializationFailedEvent e) {
        switch (e.getError()) {
            case UNKNOWN:
                controller.relayFb(Constants.CMD_ERROR_INIT_FAIL_UNKNOWN,
                    MsgType.ERROR);
                break;
            case INVALID_PATH:
                controller.relayFb(
                    String.format(Constants.CMD_ERROR_INIT_FAIL_INVALID,
                        e.getPath()), MsgType.ERROR);
                break;
            case FILE_REPLACED:
                String fb = "";
                for (FilePathPair item : e.getFilePathPairs()) {
                    fb += String.format(Constants.CMD_ERROR_INIT_FAIL_REPLACED,
                        "\n" + item.getOldFilePath(), item.getNewFilePath());
                }
                controller.relayFb(fb, MsgType.ERROR);
                break;
            default:
                break;
        }
    }

    @Subscribe
    public void handleInvalidCommandEvent(InvalidCommandEvent e) {
        controller.relayFb(
            String.format(Constants.CMD_WARNING_DONTKNOW, e.getInputString()),
            MsgType.WARNING);
        logger.fine(Constants.LOG_INVALID_COMMAND);
    }

    @Subscribe
    public void handleListDoneEvent(ListDoneEvent e) {
        switch (e.getListType()) {
            case ALL:
                controller.displayStatus = Constants.CTRL_CMD_ALL;
                break;
            case COMPLETED:
                controller.displayStatus = Constants.CTRL_CMD_COMPLETE;
                break;
            case INCOMPLETE:
                controller.displayStatus = Constants.CTRL_CMD_INCOMPLETE;
                break;
            default:
                break;
        }

        listTasks(e.getItems());
        controller.relayFb(Constants.CMD_SUCCESS_LISTED, MsgType.SUCCESS);
    }

    @Subscribe
    public void handleMarkTaskDoneEvent(MarkTaskDoneEvent e) {
        ArrayList<Integer> doneIds = e.getScreenIds();
        Collections.sort(doneIds, Comparator.reverseOrder());
        int indexCount = -1;
        for (Integer screenId : doneIds) {
            indexCount = screenId - 1;
            controller.importantList.get(indexCount).setMarkT();
            controller.importantList.get(indexCount).strikeOut();
            refreshDisplay();

            // controller.displayList(controller.displayStatus);
            // logger.fine(String.format(Constants.LOG_DELETED_SUCCESS, num));
        }
        controller.relayFb(
            String.format(Constants.CMD_SUCCESS_MARKED, indexCount + 1),
            MsgType.SUCCESS);
    }

    private void refreshDisplay() {
        appendTaskToDisplayList(new TaskAttributes());
        controller.importantList.remove(controller.importantList.size() - 1);
        // TODO Auto-generated method stub

    }

    @Subscribe
    public void handleMarkTaskFailEvent(MarkTaskFailedEvent e) {
        switch (e.getError()) {
            case UNKNOWN:
                controller.relayFb(Constants.CMD_ERROR_CANT_MARK_UNKNOWN,
                    MsgType.ERROR);
                // logger.fine(Constants.LOG_DELETE_FAIL_UNKNOWN);
                break;
            case NON_EXISTENT_ID:
                // NEED TO CHANGE TO INDEX SOON????
                for (Integer screenId : e.getInvalidIds()) {
                    controller.relayFb(String.format(
                        Constants.CMD_ERROR_CANT_MARK_NO_ID, screenId),
                        MsgType.ERROR);
                }
                // logger.fine(Constants.LOG_DELETE_FAIL_NOID);
                break;
            default:
                break;
        }
    }

    @Subscribe
    public void handleMoveDirectoryDoneEvent(MoveDirectoryDoneEvent e) {
        controller.displayList(Constants.CTRL_CMD_INCOMPLETE);
        controller.relayFb(
            String.format(Constants.CMD_SUCCESS_MOVED, e.getNewDirectory()),
            MsgType.SUCCESS);
    }

    @Subscribe
    public void handleMoveDirectoryFailEvent(MoveDirectoryFailedEvent e) {
        switch (e.getError()) {
            case UNKNOWN:
                controller.relayFb(
                    String.format(Constants.CMD_ERROR_MOVE_FAIL_UNKNOWN,
                        e.getNewDirectory()), MsgType.ERROR);
                break;
            case INVALID_PATH:
                controller.relayFb(
                    String.format(Constants.CMD_ERROR_MOVE_FAIL_INVALID,
                        e.getNewDirectory()), MsgType.ERROR);
                break;
            case FILE_REPLACED:
                String fb = "";
                for (FilePathPair item : e.getFilePathPairs()) {
                    fb += String.format(Constants.CMD_ERROR_MOVE_FAIL_REPLACED,
                        "\n" + item.getOldFilePath(), item.getNewFilePath());
                }
                controller.relayFb(fb, MsgType.ERROR);
                break;
            default:
                break;
        }
    }

    @Subscribe
    public void handleNoSurpriseEvent(NoSurpriseEvent e) {
        switch (e.getError()) {
            case UNKNOWN:
                controller.relayFb(Constants.CMD_ERROR_SURP_FAIL_UNKNOWN,
                    MsgType.ERROR);
                break;
            case NO_TASKS:
                controller.relayFb(Constants.CMD_ERROR_SURP_FAIL_NO_TASKS,
                    MsgType.ERROR);
                break;
            default:
                break;
        }
    }

    @Subscribe
    public void handleRedoFailedEvent(RedoFailedEvent e) {
        switch (e.getError()) {
            case UNKNOWN:
                controller.relayFb(Constants.CMD_ERROR_REDO_FAIL_UNKNOWN,
                    MsgType.ERROR);
                break;
            case NONTHING_TO_REDO:
                controller.relayFb(Constants.CMD_ERROR_REDO_FAIL_NO_TASKS,
                    MsgType.ERROR);
                break;
            default:
                break;
        }
    }

    @Subscribe
    public void handleRenameTaskDoneEvent(RenameTaskDoneEvent e) {
        TaskAttributes task = e.getTask();
        int count = 0;
        for (int i = 0; i < controller.importantList.size(); i++) {
            if (controller.getIdFromIndex(i) == task.getId()) {
                controller.importantList.get(i).setDescription(
                    task.getDescription());
                count = i;
                break;
            }
        }
        controller.relayFb(
            String.format(Constants.CMD_SUCCESS_RENAMED, count + 1,
                task.getDescription()), MsgType.SUCCESS);
        logger.fine(String.format(Constants.LOG_RENAMED_SUCCESS, task.getId()));
    }

    @Subscribe
    public void handleRenameTaskFailEvent(RenameTaskFailedEvent e) {
        switch (e.getError()) {
            case UNKNOWN:
                controller.relayFb(Constants.CMD_ERROR_CANT_RENAME_UNKNOWN,
                    MsgType.ERROR);
                logger.fine(Constants.LOG_RENAME_FAIL_UNKNOWN);
                break;
            case NON_EXISTENT_ID:
                // NEED TO CHANGE TO INDEX SOON????
                controller.relayFb(
                    String.format(Constants.CMD_ERROR_CANT_RENAME_NO_ID,
                        e.getScreenId()), MsgType.ERROR);
                logger.fine(Constants.LOG_RENAME_FAIL_NOID);
                break;
            case NO_CHANGES:
                controller.relayFb(
                    String.format(Constants.CMD_ERROR_CANT_RENAME_NO_CHANGES,
                        e.getDescription()), MsgType.ERROR);
                logger.fine(Constants.LOG_RENAME_FAIL_NOCHANGE);
                break;
            default:
                break;
        }
    }

    @Subscribe
    public void handleRescheduleTaskDoneEvent(RescheduleTaskDoneEvent e) {
        int count = 0;
        TaskAttributes task = e.getTask();
        for (int i = 0; i < controller.importantList.size(); i++) {
            if (controller.getIdFromIndex(i) == task.getId()) {
                controller.importantList.get(i).setTimeDate(
                    task.getStartDateTime(), task.getEndDateTime());
                count = i;
                break;
            }
        }
        controller.relayFb(
            String.format(Constants.CMD_SUCCESS_RESCHEDULED, count + 1),
            MsgType.SUCCESS);
        logger.fine(String.format(Constants.LOG_RESCHED_SUCCESS, task.getId()));
    }

    @Subscribe
    public void handleRescheduleTaskFailEvent(RescheduleTaskFailedEvent e) {
        switch (e.getError()) {
            case UNKNOWN:
                controller.relayFb(Constants.CMD_ERROR_CANT_RESCHEDULE_UNKNOWN,
                    MsgType.ERROR);
                logger.fine(Constants.LOG_RESCHE_FAIL_UNKNOWN);
                break;
            case NON_EXISTENT_ID:
                // NEED TO CHANGE TO INDEX SOON????
                controller.relayFb(
                    String.format(Constants.CMD_ERROR_CANT_RESCHEDULE_NO_ID,
                        e.getScreenId()), MsgType.ERROR);
                logger.fine(Constants.LOG_RESCHE_FAIL_NOID);
                break;
            case NO_CHANGES:
                controller.relayFb(
                    Constants.CMD_ERROR_CANT_RESCHEDULE_NO_CHANGES
                        + e.getStartDateTime() + " - to - "
                        + e.getEndDateTime() + " -!", MsgType.ERROR);
                logger.fine(Constants.LOG_RESCHE_FAIL_NOCHANGE);
                break;
            default:
                break;
        }
    }

    @Subscribe
    public void handleSearchDoneEvent(SearchDoneEvent e) {

        listTasks(e.getResults());

        controller.displayStatus = "Search ";
        for (String key : e.getKeywords()) {
            controller.displayStatus += key;
        }

        controller.setHighlights(e.getKeywords());
        controller.relayFb(Constants.CMD_SUCCESS_SEARCH, MsgType.SUCCESS);
    }

    @Subscribe
    public void handleShowDirectoryEvent(ShowDirectoryEvent e) {
        controller.relayFb(
            String.format(Constants.CMD_SUCCESS_SHOWDIRECTORY, e.getPwd()),
            MsgType.SUCCESS);
    }

    @Subscribe
    public void handleSurpriseEvent(SurpriseEvent e) {

        controller.importantList.clear();
        TaskAttributes task = e.getTask();
        appendTaskToDisplayList(task);
        controller.displayStatus = Constants.CTRL_CMD_INCOMPLETE;
        controller.relayFb(Constants.CMD_SUCCESS_SURPRISED, MsgType.SUCCESS);
    }

    @Subscribe
    public void handleUnaliasDoneEvent(UnaliasDoneEvent e) {
        controller.relayFb(
            String.format(Constants.CMD_SUCCESS_UNALIAS, e.getAlias()),
            MsgType.SUCCESS);
    }

    @Subscribe
    public void handleUnaliasFailEvent(UnaliasFailEvent e) {
        switch (e.getError()) {
            case UNKNOWN:
                controller.relayFb(
                    String.format(Constants.CMD_ERROR_CANT_UNALIAS_UNKNOWN,
                        e.getAlias()), MsgType.ERROR);
                // logger.fine(Constants.LOG_RESCHE_FAIL_UNKNOWN);
                break;
            case NON_EXISTENT_ALIAS:
                // NEED TO CHANGE TO INDEX SOON????
                controller.relayFb(
                    String.format(Constants.CMD_ERROR_CANT_UNALIAS_NO_ALIAS,
                        e.getAlias()), MsgType.ERROR);
                // logger.fine(Constants.LOG_RESCHE_FAIL_NOID);
                break;
            default:
                break;
        }
    }

    @Subscribe
    public void handleUndoFailedEvent(UndoFailedEvent e) {
        switch (e.getError()) {
            case UNKNOWN:
                controller.relayFb(Constants.CMD_ERROR_UNDO_FAIL_UNKNOWN,
                    MsgType.ERROR);
                break;
            case NONTHING_TO_UNDO:
                controller.relayFb(Constants.CMD_ERROR_UNDO_FAIL_NO_TASKS,
                    MsgType.ERROR);
                break;
            default:
                break;
        }
    }

    @Subscribe
    public void handleUnmarkTaskDoneEvent(UnmarkTaskDoneEvent e) {
        ArrayList<Integer> undoneIds = e.getScreenIds();
        Collections.sort(undoneIds, Comparator.reverseOrder());
        int indexCount = -1;
        for (Integer screenId : undoneIds) {
            indexCount = screenId - 1;
            controller.importantList.get(indexCount).setMarkF();
            controller.importantList.get(indexCount).removeStrike();
            refreshDisplay();
            // controller.displayList(controller.displayStatus);
            // logger.fine(String.format(Constants.LOG_DELETED_SUCCESS, num));
        }
        controller.relayFb(
            String.format(Constants.CMD_SUCCESS_UNMARKED, indexCount + 1),
            MsgType.SUCCESS);
    }

    @Subscribe
    public void handleUnmarkTaskFailEvent(UnmarkTaskFailEvent e) {
        switch (e.getError()) {
            case UNKNOWN:
                controller.relayFb(Constants.CMD_ERROR_CANT_UNMARK_UNKNOWN,
                    MsgType.ERROR);
                // logger.fine(Constants.LOG_DELETE_FAIL_UNKNOWN);
                break;
            case NON_EXISTENT_ID:
                // NEED TO CHANGE TO INDEX SOON????
                for (Integer screenId : e.getInvalidIds()) {
                    controller.relayFb(String.format(
                        Constants.CMD_ERROR_CANT_UNMARK_NO_ID, screenId),
                        MsgType.ERROR);
                }
                // logger.fine(Constants.LOG_DELETE_FAIL_NOID);
                break;
            default:
                break;
        }
    }

    @Subscribe
    public void handleUseDirectoryDoneEvent(UseDirectoryDoneEvent e) {
        controller.displayList(Constants.CTRL_CMD_INCOMPLETE);
        controller.relayFb(
            String.format(Constants.CMD_SUCCESS_USED, e.getNewDirectory()),
            MsgType.SUCCESS);
    }

    @Subscribe
    public void handleUseDirectoryFailEvent(UseDirectoryFailedEvent e) {
        switch (e.getError()) {
            case UNKNOWN:
                controller.relayFb(
                    String.format(Constants.CMD_ERROR_USE_FAIL_UNKNOWN,
                        e.getNewDirectory()), MsgType.ERROR);
                break;
            case INVALID_PATH:
                controller.relayFb(
                    String.format(Constants.CMD_ERROR_USE_FAIL_INVALID,
                        e.getNewDirectory()), MsgType.ERROR);
                break;
            case FILE_REPLACED:
                String fb = "";
                for (FilePathPair item : e.getFilePathPairs()) {
                    fb += String.format(Constants.CMD_ERROR_USE_FAIL_REPLACED,
                        "\n" + item.getOldFilePath(), item.getNewFilePath());
                }
                controller.relayFb(fb, MsgType.ERROR);
                break;
            default:
                break;
        }
    }

    public void setController(MainController controller) {
        this.controller = controller;
    }

    /**
     * Sets the display list to the given ArrayList of tasks.
     *
     * @param tasks
     *            the ArrayList of tasks to be displayed
     */
    private void listTasks(ArrayList<TaskAttributes> tasks) {
        controller.importantList.clear();
        for (TaskAttributes task : tasks) {
            appendTaskToDisplayList(task);
        }
    }

    /**
     * Appends a task to the list of tasks displayed.
     *
     * @param task
     *            the task to be appended
     * @return the on-screen ID of the task appended
     */
    private int appendTaskToDisplayList(TaskAttributes task) {
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
        return onScreenId;
    }
}
