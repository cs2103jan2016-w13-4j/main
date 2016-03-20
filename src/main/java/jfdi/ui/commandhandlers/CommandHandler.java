package jfdi.ui.commandhandlers;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import edu.emory.mathcs.backport.java.util.Collections;
import jfdi.common.utilities.JfdiLogger;
import jfdi.logic.events.AddTaskDoneEvent;
import jfdi.logic.events.AddTaskFailEvent;
import jfdi.logic.events.AliasDoneEvent;
import jfdi.logic.events.AliasFailEvent;
import jfdi.logic.events.DeleteTaskDoneEvent;
import jfdi.logic.events.DeleteTaskFailEvent;
import jfdi.logic.events.ExitCalledEvent;
import jfdi.logic.events.InvalidCommandEvent;
import jfdi.logic.events.ListDoneEvent;
import jfdi.logic.events.MarkTaskDoneEvent;
import jfdi.logic.events.MarkTaskFailEvent;
import jfdi.logic.events.RenameTaskDoneEvent;
import jfdi.logic.events.RenameTaskFailEvent;
import jfdi.logic.events.RescheduleTaskDoneEvent;
import jfdi.logic.events.RescheduleTaskFailEvent;
import jfdi.logic.events.SearchDoneEvent;
import jfdi.logic.events.ShowDirectoryEvent;
import jfdi.logic.events.UnaliasDoneEvent;
import jfdi.logic.events.UnaliasFailEvent;
import jfdi.logic.events.UnmarkTaskDoneEvent;
import jfdi.logic.events.UnmarkTaskFailEvent;
import jfdi.storage.apis.TaskAttributes;
import jfdi.ui.Constants;
import jfdi.ui.Constants.MsgType;
import jfdi.ui.ListItem;
import jfdi.ui.MainController;

public class CommandHandler {

    public MainController controller;
    public Logger logger = JfdiLogger.getLogger();

    @Subscribe
    public void handleAddTaskDoneEvent(AddTaskDoneEvent e) {
        TaskAttributes task = e.getTask();
        int index = controller.importantList.size() + 1;
        ListItem listItem = new ListItem(index, task, false);
        controller.importantList.add(listItem);
        controller.relayFb(String.format(Constants.CMD_SUCCESS_ADDED, index, task.getDescription()), MsgType.SUCCESS);
        logger.fine(String.format(Constants.LOG_ADDED_SUCCESS, task.getId()));
    }

    @Subscribe
    public void handleAddTaskFailEvent(AddTaskFailEvent e) {
        switch (e.getError()) {
            case UNKNOWN:
                controller.relayFb(Constants.CMD_ERROR_CANT_ADD_UNKNOWN, MsgType.ERROR);
                logger.fine(String.format(Constants.LOG_ADD_FAIL_UNKNOWN));
                break;
            case EMPTY_DESCRIPTION:
                controller.relayFb(Constants.CMD_ERROR_CANT_ADD_EMPTY, MsgType.ERROR);
                logger.fine(String.format(Constants.LOG_ADD_FAIL_EMPTY));
                break;
            default:
                break;
        }
    }

    @Subscribe
    public void handleAliasDoneEvent(AliasDoneEvent e) {
        controller.relayFb(String.format(Constants.CMD_SUCCESS_ALIAS, e.getAlias(), e.getCommand()), MsgType.SUCCESS);
    }

    @Subscribe
    public void handleAliasFailEvent(AliasFailEvent e) {
        switch (e.getError()) {
            case INVALID_PARAMETERS:
                controller.relayFb(String.format(Constants.CMD_ERROR_CANT_ALIAS_INVALID, e.getAlias(), e.getCommand()),
                        MsgType.ERROR);
                //logger.fine(String.format(format, args));
                break;
            case DUPLICATED_ALIAS:
                controller.relayFb(String.format(Constants.CMD_ERROR_CANT_ALIAS_DUPLICATED, e.getAlias()),
                        MsgType.ERROR);
                //logger.fine(String.format(format, args));
                break;
            case UNKNOWN:
                controller.relayFb(String.format(Constants.CMD_ERROR_CANT_ALIAS_UNKNOWN, e.getCommand()),
                        MsgType.ERROR);
                //logger.fine(String.format(format, args));
                break;
            default:
                break;
        }
    }

    @Subscribe
    public void handleDeleteTaskDoneEvent(DeleteTaskDoneEvent e) {
        ArrayList<Integer> deletedIds = e.getDeletedIds();
        Collections.sort(deletedIds);

        int indexCount = 0;
        for (Integer num : deletedIds) {
            while (controller.getIdFromIndex(indexCount) != num) {
                indexCount++;
            }
            controller.importantList.remove(indexCount);
            logger.fine(String.format(Constants.LOG_DELETED_SUCCESS, num));
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
    public void handleDeleteTaskFailEvent(DeleteTaskFailEvent e) {
        switch (e.getError()) {
            case UNKNOWN:
                controller.relayFb(Constants.CMD_ERROR_CANT_DELETE_UNKNOWN, MsgType.ERROR);
                logger.fine(Constants.LOG_DELETE_FAIL_UNKNOWN);
                break;
            case NON_EXISTENT_ID:
                //NEED TO CHANGE TO INDEX SOON????
                for (Integer num : e.getInvalidIds()) {
                    controller.relayFb(String.format(Constants.CMD_ERROR_CANT_DELETE_NO_ID, num), MsgType.ERROR);
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
    public void handleInvalidCommandEvent(InvalidCommandEvent e) {
        controller.relayFb(String.format(Constants.CMD_WARNING_DONTKNOW, e.getInputString()), MsgType.WARNING);
        logger.fine(Constants.LOG_INVALID_COMMAND);
    }

    @Subscribe
    public void handleListDoneEvent(ListDoneEvent e) {

        controller.importantList.clear();

        int count = 1;
        for (TaskAttributes item : e.getItems()) {
            ListItem listItem;
            if (item.isCompleted()) {
                listItem = new ListItem(count, item, true);
            } else {
                listItem = new ListItem(count, item, false);
            }
            controller.importantList.add(listItem);
            count++;
        }
        controller.relayFb(Constants.CMD_SUCCESS_LISTED, MsgType.SUCCESS);
    }

    @Subscribe
    public void handleMarkTaskDoneEvent(MarkTaskDoneEvent e) {
        ArrayList<Integer> doneIds = e.getTaskIds();
        Collections.sort(doneIds);
        int indexCount = 0;
        for (Integer num : doneIds) {
            while (controller.getIdFromIndex(indexCount) != num) {
                indexCount++;
            }
            controller.importantList.get(indexCount).setMarkT();
            controller.importantList.get(indexCount).strikeOut();
            //logger.fine(String.format(Constants.LOG_DELETED_SUCCESS, num));
        }
        controller.relayFb(String.format(Constants.CMD_SUCCESS_MARKED, indexCount + 1), MsgType.SUCCESS);
    }

    @Subscribe
    public void handleMarkTaskFailEvent(MarkTaskFailEvent e) {
        switch (e.getError()) {
            case UNKNOWN:
                controller.relayFb(Constants.CMD_ERROR_CANT_MARK_UNKNOWN, MsgType.ERROR);
                //logger.fine(Constants.LOG_DELETE_FAIL_UNKNOWN);
                break;
            case NON_EXISTENT_ID:
                //NEED TO CHANGE TO INDEX SOON????
                for (Integer num : e.getInvalidIds()) {
                    controller.relayFb(String.format(Constants.CMD_ERROR_CANT_MARK_NO_ID, num), MsgType.ERROR);
                }
                //logger.fine(Constants.LOG_DELETE_FAIL_NOID);
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
                controller.importantList.get(i).setDescription(task.getDescription());
                count = i;
                break;
            }
        }
        controller.relayFb(String.format(
                Constants.CMD_SUCCESS_RENAMED, count + 1, task.getDescription()), MsgType.SUCCESS);
        logger.fine(String.format(Constants.LOG_RENAMED_SUCCESS, task.getId()));
    }

    @Subscribe
    public void handleRenameTaskFailEvent(RenameTaskFailEvent e) {
        switch (e.getError()) {
            case UNKNOWN:
                controller.relayFb(Constants.CMD_ERROR_CANT_RENAME_UNKNOWN, MsgType.ERROR);
                logger.fine(Constants.LOG_RENAME_FAIL_UNKNOWN);
                break;
            case NON_EXISTENT_ID:
                //NEED TO CHANGE TO INDEX SOON????
                controller.relayFb(String.format(Constants.CMD_ERROR_CANT_RENAME_NO_ID, e.getTaskId()), MsgType.ERROR);
                logger.fine(Constants.LOG_RENAME_FAIL_NOID);
                break;
            case NO_CHANGES:
                controller.relayFb(String.format(
                        Constants.CMD_ERROR_CANT_RENAME_NO_CHANGES, e.getDescription()), MsgType.ERROR);
                logger.fine(Constants.LOG_RENAME_FAIL_NOCHANGE);
                break;
            default:
                break;
        }
    }

    @Subscribe
    public void handleRescheduleTaskDoneEvent(RescheduleTaskDoneEvent e) {
        int count = 0;
        for (int i = 0; i < controller.importantList.size(); i++) {
            if (controller.getIdFromIndex(i) == e.getTaskId()) {
                controller.importantList.get(i).setTimeDate(e.getStartDateTime(), e.getEndDateTime());
                count = i;
                break;
            }
        }
        controller.relayFb(String.format(Constants.CMD_SUCCESS_RESCHEDULED, count + 1), MsgType.SUCCESS);
        logger.fine(String.format(Constants.LOG_RESCHED_SUCCESS, e.getTaskId()));
    }

    @Subscribe
    public void handleRescheduleTaskFailEvent(RescheduleTaskFailEvent e) {
        switch (e.getError()) {
            case UNKNOWN:
                controller.relayFb(Constants.CMD_ERROR_CANT_RESCHEDULE_UNKNOWN, MsgType.ERROR);
                logger.fine(Constants.LOG_RESCHE_FAIL_UNKNOWN);
                break;
            case NON_EXISTENT_ID:
                //NEED TO CHANGE TO INDEX SOON????
                controller.relayFb(String.format(
                        Constants.CMD_ERROR_CANT_RESCHEDULE_NO_ID, e.getTaskId()), MsgType.ERROR);
                logger.fine(Constants.LOG_RESCHE_FAIL_NOID);
                break;
            case NO_CHANGES:
                controller.relayFb(
                        Constants.CMD_ERROR_CANT_RESCHEDULE_NO_CHANGES + e.getStartDateTime() + " - to - "
                                + e.getEndDateTime() + " -!", MsgType.ERROR);
                logger.fine(Constants.LOG_RESCHE_FAIL_NOCHANGE);
                break;
            default:
                break;
        }
    }

    @Subscribe
    public void handleSearchDoneEvent(SearchDoneEvent e) {

        controller.importantList.clear();

        int count = 1;
        for (TaskAttributes item : e.getResults()) {
            ListItem listItem;
            if (item.isCompleted()) {
                listItem = new ListItem(count, item, true);
            } else {
                listItem = new ListItem(count, item, false);
            }
            controller.importantList.add(listItem);
            count++;
        }

        System.out.println(e.getKeywords().isEmpty());

        controller.setHighlights(e.getKeywords());
        controller.relayFb(Constants.CMD_SUCCESS_SEARCH, MsgType.SUCCESS);
    }

    @Subscribe
    public void handleShowDirectoryEvent(ShowDirectoryEvent e) {
        controller.relayFb(String.format(Constants.CMD_SUCCESS_SHOWDIRECTORY, e.getPwd()), MsgType.SUCCESS);
    }

    @Subscribe
    public void handleUnaliasDoneEvent(UnaliasDoneEvent e) {
        controller.relayFb(String.format(Constants.CMD_SUCCESS_UNALIAS, e.getAlias()), MsgType.SUCCESS);
    }

    @Subscribe
    public void handleUnaliasFailEvent(UnaliasFailEvent e) {
        switch (e.getError()) {
            case UNKNOWN:
                controller.relayFb(String.format(Constants.CMD_ERROR_CANT_UNALIAS_UNKNOWN, e.getAlias()),
                        MsgType.ERROR);
                //logger.fine(Constants.LOG_RESCHE_FAIL_UNKNOWN);
                break;
            case NON_EXISTENT_ALIAS:
                //NEED TO CHANGE TO INDEX SOON????
                controller.relayFb(String.format(
                        Constants.CMD_ERROR_CANT_UNALIAS_NO_ALIAS, e.getAlias()), MsgType.ERROR);
                //logger.fine(Constants.LOG_RESCHE_FAIL_NOID);
                break;
            default:
                break;
        }
    }

    @Subscribe
    public void handleUnmarkTaskDoneEvent(UnmarkTaskDoneEvent e) {
        ArrayList<Integer> undoneIds = e.getTaskIds();
        Collections.sort(undoneIds);
        int indexCount = 0;
        for (Integer num : undoneIds) {
            while (controller.getIdFromIndex(indexCount) != num) {
                indexCount++;
            }
            controller.importantList.get(indexCount).setMarkF();
            controller.importantList.get(indexCount).removeStrike();
            //logger.fine(String.format(Constants.LOG_DELETED_SUCCESS, num));
        }
        controller.relayFb(String.format(Constants.CMD_SUCCESS_UNMARKED, indexCount + 1), MsgType.SUCCESS);
    }

    @Subscribe
    public void handleUnmarkTaskFailEvent(UnmarkTaskFailEvent e) {
        switch (e.getError()) {
            case UNKNOWN:
                controller.relayFb(Constants.CMD_ERROR_CANT_UNMARK_UNKNOWN, MsgType.ERROR);
                //logger.fine(Constants.LOG_DELETE_FAIL_UNKNOWN);
                break;
            case NON_EXISTENT_ID:
                //NEED TO CHANGE TO INDEX SOON????
                for (Integer num : e.getInvalidIds()) {
                    controller.relayFb(String.format(Constants.CMD_ERROR_CANT_UNMARK_NO_ID, num), MsgType.ERROR);
                }
                //logger.fine(Constants.LOG_DELETE_FAIL_NOID);
                break;
            default:
                break;
        }
    }

    public void setController(MainController controller) {
        this.controller = controller;
    }
}
