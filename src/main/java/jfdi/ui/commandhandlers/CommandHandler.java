package jfdi.ui.commandhandlers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import edu.emory.mathcs.backport.java.util.Collections;
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
import jfdi.logic.events.FilesReplacedEvent;
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
import jfdi.logic.events.UnaliasFailedEvent;
import jfdi.logic.events.UndoFailedEvent;
import jfdi.logic.events.UnmarkTaskDoneEvent;
import jfdi.logic.events.UnmarkTaskFailedEvent;
import jfdi.logic.events.UseDirectoryDoneEvent;
import jfdi.logic.events.UseDirectoryFailedEvent;
import jfdi.logic.interfaces.Command;
import jfdi.storage.apis.TaskAttributes;
import jfdi.storage.exceptions.FilePathPair;
import jfdi.ui.Constants;
import jfdi.ui.Constants.ListStatus;
import jfdi.ui.Constants.MsgType;
import jfdi.ui.MainController;
import jfdi.ui.items.ListItem;

public class CommandHandler {

    public MainController controller;
    public Logger logger = JfdiLogger.getLogger();

    @Subscribe
    public void handleAddTaskDoneEvent(AddTaskDoneEvent e) {
        if (controller.isInternalCall()) {
            // Add any method calls strictly for internal calls here
            controller.completeInternalCall();
            return;
        }

        TaskAttributes task = e.getTask();
        appendTaskToDisplayList(task, true);
        if (shouldSort()) {
            sortDisplayList();
        }
        controller.relayFb(
            String.format(Constants.CMD_SUCCESS_ADDED, task.getDescription()),
            MsgType.SUCCESS);
        logger.fine(String.format(Constants.LOG_ADDED_SUCCESS, task.getId()));
        controller.updateNotiBubbles();
        controller.listMain.scrollTo(controller.importantList.size() - 1);
    }

    @Subscribe
    public void handleAddTaskFailEvent(AddTaskFailedEvent e) {
        if (controller.isInternalCall()) {
            // Add any method calls strictly for internal calls here
            controller.completeInternalCall();
            return;
        }

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
            case DUPLICATED_TASK:
                controller.relayFb(Constants.CMD_ERROR_CANT_ADD_DUPLICATE,
                    MsgType.ERROR);
                logger.fine(String.format(Constants.LOG_ADD_FAIL_DUPLICATE));
                break;
            default:
                break;
        }
    }

    @Subscribe
    public void handleAliasDoneEvent(AliasDoneEvent e) {
        if (controller.isInternalCall()) {
            // Add any method calls strictly for internal calls here
            controller.completeInternalCall();
            return;
        }

        controller.relayFb(
            String.format(Constants.CMD_SUCCESS_ALIAS, e.getAlias(),
                e.getCommand()), MsgType.SUCCESS);
    }

    @Subscribe
    public void handleAliasFailEvent(AliasFailedEvent e) {
        if (controller.isInternalCall()) {
            // Add any method calls strictly for internal calls here
            controller.completeInternalCall();
            return;
        }

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
        if (controller.isInternalCall()) {
            // Add any method calls strictly for internal calls here
            controller.completeInternalCall();
            return;
        }

        Class<? extends Command> cmdType = e.getCommandType();
        switchContext(controller.displayStatus, true);
        controller.relayFb(
            String.format(Constants.CMD_SUCCESS_REDONE, cmdType.toString()),
            MsgType.SUCCESS);
        controller.updateNotiBubbles();
    }

    @Subscribe
    public void handleCommandUndoneEvent(CommandUndoneEvent e) {
        if (controller.isInternalCall()) {
            // Add any method calls strictly for internal calls here
            controller.completeInternalCall();
            return;
        }

        Class<? extends Command> cmdType = e.getCommandType();
        switchContext(controller.displayStatus, true);
        controller.relayFb(
            String.format(Constants.CMD_SUCCESS_UNDONE, cmdType.toString()),
            MsgType.SUCCESS);
        controller.updateNotiBubbles();
    }

    @Subscribe
    public void handleDeleteTaskDoneEvent(DeleteTaskDoneEvent e) {
        if (controller.isInternalCall()) {
            // Add any method calls strictly for internal calls here
            controller.completeInternalCall();
            return;
        }

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
        controller.updateNotiBubbles();
    }

    @Subscribe
    public void handleDeleteTaskFailEvent(DeleteTaskFailedEvent e) {
        if (controller.isInternalCall()) {
            // Add any method calls strictly for internal calls here
            controller.completeInternalCall();
            return;
        }

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
        if (controller.isInternalCall()) {
            // Add any method calls strictly for internal calls here
            controller.completeInternalCall();
            return;
        }

        System.out.printf("\nMoriturus te saluto.\n");
        System.exit(0);
        logger.fine(Constants.LOG_USER_EXIT);
    }

    @Subscribe
    public void handleHelpRequestEvent(HelpRequestedEvent e) {
        if (controller.isInternalCall()) {
            // Add any method calls strictly for internal calls here
            controller.completeInternalCall();
            return;
        }

        switchContext(ListStatus.HELP, false);
        controller.showHelpDisplay();
        controller.relayFb(Constants.CMD_SUCCESS_HELP, MsgType.SUCCESS);
    }

    @Subscribe
    public void handleFilesReplacedEvent(FilesReplacedEvent e) {
        String fb = "";
        for (FilePathPair item : e.getFilePathPairs()) {
            fb += String.format("\n" + Constants.CMD_ERROR_INIT_FAIL_REPLACED,
                item.getOldFilePath(), item.getNewFilePath());
        }
        controller.appendFb(fb, MsgType.WARNING);
        controller.updateNotiBubbles();
    }

    @Subscribe
    public void handleInitializationFailedEvent(InitializationFailedEvent e) {
        if (controller.isInternalCall()) {
            // Add any method calls strictly for internal calls here
            controller.completeInternalCall();
            return;
        }

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
            default:
                break;
        }
    }

    @Subscribe
    public void handleInvalidCommandEvent(InvalidCommandEvent e) {
        if (controller.isInternalCall()) {
            // Add any method calls strictly for internal calls here
            controller.completeInternalCall();
            return;
        }

        controller.relayFb(
            String.format(Constants.CMD_WARNING_DONTKNOW, e.getInputString()),
            MsgType.WARNING);
        logger.fine(Constants.LOG_INVALID_COMMAND);
    }

    @Subscribe
    public void handleListDoneEvent(ListDoneEvent e) {
        updateBubble(e);
        if (controller.isInternalCall()) {
            // Add any method calls strictly for internal calls here
            controller.completeInternalCall();
            return;
        }

        switch (e.getListType()) {
            case ALL:
                switchContext(ListStatus.ALL, false);
                break;
            case COMPLETED:
                switchContext(ListStatus.COMPLETE, false);
                break;
            case INCOMPLETE:
                switchContext(ListStatus.INCOMPLETE, false);
                break;
            case OVERDUE:
                switchContext(ListStatus.OVERDUE, false);
                break;
            case UPCOMING:
                switchContext(ListStatus.UPCOMING, false);
                break;
            default:
                break;

        }
        listTasks(e.getItems(), false);
        controller.relayFb(Constants.CMD_SUCCESS_LISTED, MsgType.SUCCESS);
    }

    @Subscribe
    public void handleMarkTaskDoneEvent(MarkTaskDoneEvent e) {
        if (controller.isInternalCall()) {
            // Add any method calls strictly for internal calls here
            controller.completeInternalCall();
            return;
        }

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
        controller.updateNotiBubbles();
        controller.listMain.scrollTo(indexCount);
    }

    private void refreshDisplay() {
        controller.listMain.refresh();
    }

    @Subscribe
    public void handleMarkTaskFailEvent(MarkTaskFailedEvent e) {
        if (controller.isInternalCall()) {
            // Add any method calls strictly for internal calls here
            controller.completeInternalCall();
            return;
        }

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
        if (controller.isInternalCall()) {
            // Add any method calls strictly for internal calls here
            controller.completeInternalCall();
            return;
        }

        switchContext(ListStatus.INCOMPLETE, true);
        controller.relayFb(
            String.format(Constants.CMD_SUCCESS_MOVED, e.getNewDirectory()),
            MsgType.SUCCESS);
        controller.updateNotiBubbles();
    }

    @Subscribe
    public void handleMoveDirectoryFailEvent(MoveDirectoryFailedEvent e) {
        if (controller.isInternalCall()) {
            // Add any method calls strictly for internal calls here
            controller.completeInternalCall();
            return;
        }

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
            default:
                break;
        }
    }

    @Subscribe
    public void handleNoSurpriseEvent(NoSurpriseEvent e) {
        if (controller.isInternalCall()) {
            // Add any method calls strictly for internal calls here
            controller.completeInternalCall();
            return;
        }

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
        if (controller.isInternalCall()) {
            // Add any method calls strictly for internal calls here
            controller.completeInternalCall();
            return;
        }

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
        if (controller.isInternalCall()) {
            // Add any method calls strictly for internal calls here
            controller.completeInternalCall();
            return;
        }

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
        controller.updateNotiBubbles();
        controller.listMain.scrollTo(count);
    }

    @Subscribe
    public void handleRenameTaskFailEvent(RenameTaskFailedEvent e) {
        if (controller.isInternalCall()) {
            // Add any method calls strictly for internal calls here
            controller.completeInternalCall();
            return;
        }

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
            case DUPLICATED_TASK:
                controller.relayFb(Constants.CMD_ERROR_CANT_RENAME_DUPLICATE,
                    MsgType.ERROR);
                logger.fine(String.format(Constants.LOG_RENAME_FAIL_DUPLICATE));
                break;
            default:
                break;
        }
    }

    @Subscribe
    public void handleRescheduleTaskDoneEvent(RescheduleTaskDoneEvent e) {
        if (controller.isInternalCall()) {
            // Add any method calls strictly for internal calls here
            controller.completeInternalCall();
            return;
        }

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
        if (shouldSort()) {
            sortDisplayList();
        }
        controller.relayFb(
            String.format(Constants.CMD_SUCCESS_RESCHEDULED, count + 1),
            MsgType.SUCCESS);
        logger.fine(String.format(Constants.LOG_RESCHED_SUCCESS, task.getId()));
        controller.updateNotiBubbles();
        controller.listMain.scrollTo(count);
    }

    @Subscribe
    public void handleRescheduleTaskFailEvent(RescheduleTaskFailedEvent e) {
        if (controller.isInternalCall()) {
            // Add any method calls strictly for internal calls here
            controller.completeInternalCall();
            return;
        }

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
            case DUPLICATED_TASK:
                controller.relayFb(
                    Constants.CMD_ERROR_CANT_RESCHEDULE_DUPLICATE,
                    MsgType.ERROR);
                logger.fine(String.format(Constants.LOG_RESCHE_FAIL_DUPLICATE));
                break;
            default:
                break;
        }
    }

    @Subscribe
    public void handleSearchDoneEvent(SearchDoneEvent e) {
        if (controller.isInternalCall()) {
            // Add any method calls strictly for internal calls here
            controller.completeInternalCall();
            return;
        }

        listTasks(e.getResults(), false);

        switchContext(ListStatus.SEARCH, false);

        for (String key : e.getKeywords()) {
            controller.searchCmd += key + " ";
        }

        // controller.switchTabSkin();
        controller.setHighlights(e.getKeywords());
        controller.relayFb(Constants.CMD_SUCCESS_SEARCH, MsgType.SUCCESS);
        controller.updateNotiBubbles();
    }

    @Subscribe
    public void handleShowDirectoryEvent(ShowDirectoryEvent e) {
        if (controller.isInternalCall()) {
            // Add any method calls strictly for internal calls here
            controller.completeInternalCall();
            return;
        }

        controller.relayFb(
            String.format(Constants.CMD_SUCCESS_SHOWDIRECTORY, e.getPwd()),
            MsgType.SUCCESS);
        controller.updateNotiBubbles();
    }

    @Subscribe
    public void handleSurpriseEvent(SurpriseEvent e) {
        if (controller.isInternalCall()) {
            // Add any method calls strictly for internal calls here
            controller.completeInternalCall();
            return;
        }

        controller.importantList.clear();
        TaskAttributes task = e.getTask();
        appendTaskToDisplayList(task, false);
        switchContext(ListStatus.ALL, false);
        switchContext(ListStatus.SURPRISE, false);
        controller.relayFb(Constants.CMD_SUCCESS_SURPRISED, MsgType.SUCCESS);
        controller.updateNotiBubbles();
    }

    @Subscribe
    public void handleUnaliasDoneEvent(UnaliasDoneEvent e) {
        if (controller.isInternalCall()) {
            // Add any method calls strictly for internal calls here
            controller.completeInternalCall();
            return;
        }

        controller.relayFb(
            String.format(Constants.CMD_SUCCESS_UNALIAS, e.getAlias()),
            MsgType.SUCCESS);
    }

    @Subscribe
    public void handleUnaliasFailEvent(UnaliasFailedEvent e) {
        if (controller.isInternalCall()) {
            // Add any method calls strictly for internal calls here
            controller.completeInternalCall();
            return;
        }

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
        if (controller.isInternalCall()) {
            // Add any method calls strictly for internal calls here
            controller.completeInternalCall();
            return;
        }

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
        if (controller.isInternalCall()) {
            // Add any method calls strictly for internal calls here
            controller.completeInternalCall();
            return;
        }

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
        controller.updateNotiBubbles();
        controller.listMain.scrollTo(indexCount);
    }

    @Subscribe
    public void handleUnmarkTaskFailEvent(UnmarkTaskFailedEvent e) {
        if (controller.isInternalCall()) {
            // Add any method calls strictly for internal calls here
            controller.completeInternalCall();
            return;
        }

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
        if (controller.isInternalCall()) {
            // Add any method calls strictly for internal calls here
            controller.completeInternalCall();
            return;
        }

        switchContext(ListStatus.INCOMPLETE, true);
        controller.relayFb(
            String.format(Constants.CMD_SUCCESS_USED, e.getNewDirectory()),
            MsgType.SUCCESS);
        controller.updateNotiBubbles();
    }

    @Subscribe
    public void handleUseDirectoryFailEvent(UseDirectoryFailedEvent e) {
        if (controller.isInternalCall()) {
            // Add any method calls strictly for internal calls here
            controller.completeInternalCall();
            return;
        }

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
            default:
                break;
        }
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
