// @@author A0129538W

package jfdi.ui;

import jfdi.ui.items.ListItem;

public class Constants {

    public static final String PRODUCT_NAME = "J.F.D.I.";

    public static final String UI_MESSAGE_GREETING = "\nJ.F.D.I. : Hello there! Nice to see you again! :)";
    public static final String UI_MESSAGE_WELCOME = "\nJ.F.D.I. : If you need help, just type \"help\" :)";
    public static final String UI_MESSAGE_RESPONSE = "\nJ.F.D.I. : %1$s";
    public static final String UI_MESSAGE_WARNING = "\nWarning: %1$s";
    public static final String UI_MESSAGE_ERROR = "\nError: %1$s";
    public static final String UI_MESSAGE_QUIT = "\nBye Bye! See you next time! :)";

    public static final String URL_ROOT_PATH = "/ui/RootLayout.fxml";
    public static final String URL_LIST_PATH = "/ui/ListLayout.fxml";
    public static final String URL_HELP_PATH = "/ui/HelpItem.fxml";
    public static final String URL_ITEM_PATH = "/ui/ListItem.fxml";
    public static final String URL_HEADER_PATH = "/ui/ListHeader.fxml";
    public static final String URL_HAMSMITH_PATH = "/ui/fonts/HammersmithOne.ttf";
    public static final String URL_RALEWAY_PATH = "/ui/fonts/raleway.heavy.ttf";
    public static final String URL_LOGO_PATH = "/ui/images/JFDI_sq.png";

    public static final String CTRL_CMD_PROMPT_TEXT = "(Hello! Please let me know what I can do for you!)";
    public static final String CTRL_CMD_INCOMPLETE = "list";
    public static final String CTRL_CMD_OVERDUE = "list overdue";
    public static final String CTRL_CMD_UPCOMING = "list upcoming";
    public static final String CTRL_CMD_ALL = "list all";
    public static final String CTRL_CMD_COMPLETE = "list completed";
    public static final String CTRL_CMD_SURPRISE = "surprise";
    public static final String CTRL_CMD_SEARCH = "search ";
    public static final String CTRL_CMD_SURPRISE_YAY = "yay";
    public static final String CTRL_CMD_SURPRISE_NAY = "nay";
    public static final String CTRL_CMD_HELP = "help";
    public static final ListItem HEADER_OVERDUE = new ListItem(ListStatus.OVERDUE.toString());
    public static final ListItem HEADER_UPCOMING = new ListItem(ListStatus.UPCOMING.toString());
    public static final ListItem HEADER_OTHERS = new ListItem(ListStatus.OTHERS.toString());

    public enum ListStatus {
        INCOMPLETE, OVERDUE, UPCOMING, ALL, COMPLETE, SEARCH, SURPRISE, SURPRISE_YAY, HELP, OTHERS
    }

    public enum MsgType {
        SUCCESS, WARNING, ERROR, EXIT
    }

    public static final String CMD_ERROR_CANT_ADD_EMPTY = "Cannot add an empty task!";
    public static final String CMD_ERROR_CANT_ADD_DUPLICATE = "Save the trouble. This task already exists :)";
    public static final String CMD_ERROR_CANT_ALIAS_INVALID = "- %s - is an invalid alias for - %s -";
    public static final String CMD_ERROR_CANT_ALIAS_DUPLICATED = " - %s - is an duplicated alias.";
    public static final String CMD_ERROR_CANT_DELETE_NO_ID = "Some stupid error occurred. Cannot delete task(s)!";
    public static final String CMD_ERROR_INIT_FAIL_INVALID = "The folder %s doesn't work. Choose another one!";
    public static final String CMD_ERROR_INIT_FAIL_REPLACED = "Your file %s was moved to %s.";
    public static final String CMD_ERROR_CANT_MARK_NO_ID = "Cannot mark task as completed. The ID #%d does not exist!!";
    public static final String CMD_ERROR_MOVE_FAIL_INVALID = "Cannot move to the folder %s . Choose another one!";
    public static final String CMD_ERROR_MOVE_FAIL_REPLACED =
            "The folder %s is occupied. File has been moved to %s instead!";
    public static final String CMD_ERROR_SURP_FAIL_NO_TASKS =
            "There's really nothing much that you can do now.. take a rest or add more tasks! :D";
    public static final String CMD_ERROR_REDO_FAIL_NO_TASKS = "Cannot find any previous task to redo!";
    public static final String CMD_ERROR_CANT_RENAME_NO_ID = "Cannot rename task. The ID #%d does not exist!";
    public static final String CMD_ERROR_CANT_RENAME_NO_CHANGES = "No difference between new and old name - %s -!";
    public static final String CMD_ERROR_CANT_RENAME_DUPLICATE = "Save the trouble. This task already exists :)";
    public static final String CMD_ERROR_CANT_RESCHEDULE_NO_ID = "Cannot reschedule task. The ID #%d does not exist!";
    public static final String CMD_ERROR_CANT_RESCHEDULE_NO_CHANGES = "No difference between new and old schedule!";
    public static final String CMD_ERROR_CANT_RESCHEDULE_DUPLICATE = "Save the trouble. This task already exists :)";
    public static final String CMD_ERROR_CANT_UNALIAS_NO_ALIAS =
            "Cannot remove alias. The alias - %s - does not exist!";
    public static final String CMD_ERROR_UNDO_FAIL_NO_TASKS = "Cannot find any previous task to undo!";
    public static final String CMD_ERROR_CANT_UNMARK_NO_ID = "Cannot unmark task. The ID #%d does not exist!!";
    public static final String CMD_ERROR_USE_FAIL_INVALID =
            "Cannot use the data at %s . Choose another one!";
    public static final String CMD_ERROR_USE_FAIL_REPLACED =
            "The folder %s contains invalid files. Data has been moved to %s instead!";

    public static final String CMD_SUCCESS_ADDED = "Task #%d - %s - added! :)";
    public static final String CMD_SUCCESS_ALIAS = "Alias - %s - is created for command - %s -! :)";
    public static final String CMD_SUCCESS_REDONE = "Previous command redone! :)";
    public static final String CMD_SUCCESS_UNDONE = "Previous command undone! :)";
    public static final String CMD_SUCCESS_DELETED_1 = "Task #%d deleted! :)";
    public static final String CMD_SUCCESS_DELETED_2 = "Tasks %s deleted! :)";
    public static final String CMD_SUCCESS_HELP = "TO THE RESCUE!!! :D";
    public static final String CMD_SUCCESS_LISTED = "Here is your requested %s list! :)";
    public static final String CMD_SUCCESS_MARKED_1 = "Task #%d completed! Good job! :)";
    public static final String CMD_SUCCESS_MARKED_2 = "Tasks %s completed! Good job! :)";
    public static final String CMD_SUCCESS_MOVED = "The location of your files has been moved to %s ! :)";
    public static final String CMD_SUCCESS_RENAMED = "Task #%d renamed to - %s -! :)";
    public static final String CMD_SUCCESS_RESCHEDULED = "Task #%d rescheduled and shifted to #%d ! :)";
    public static final String CMD_SUCCESS_SEARCH_1 = "Here are your search results for keyword - %s -! :)";
    public static final String CMD_SUCCESS_SEARCH_2 = "Here are your search results for keywords - %s -! :)";
    public static final String CMD_SUCCESS_SHOWDIRECTORY = "Your current directory is %s ";
    public static final String CMD_SUCCESS_SURPRISED = "SURPRISE!!!TAAAADAAAA!!! :D";
    public static final String CMD_SUCCESS_SURPRISED_YAY = "Way to go! You can do it!!! :D";
    public static final String CMD_SUCCESS_UNALIAS = "Alias - %s - is removed! :)";
    public static final String CMD_SUCCESS_UNMARKED_1 = "Task #%d marked as incomplete! :)";
    public static final String CMD_SUCCESS_UNMARKED_2 = "Tasks %s marked as incomplete! :)";
    public static final String CMD_SUCCESS_USED = "You are now using data from %s ! :)";

    public static final String CMD_WARNING_DONTKNOW = "Sorry, I do not understand what you mean by \"%s\" :(";

    public static final String LOG_FXML_PATH = "Loaded %s from %s'.";
    public static final String LOG_SUCCESS_LISTED = "List requested.";
    public static final String LOG_USER_EXIT = "Exited from JFDI.";
    public static final String LOG_INVALID_COMMAND = "Invalid command detected.";
    public static final String LOG_ADDED_SUCCESS = "Displayed successful add event for task #%d.";
    public static final String LOG_ADD_FAIL_EMPTY = "An task failed to add due to empty description.";
    public static final String LOG_ADD_FAIL_DUPLICATE = "A duplicate task was not added.";
    public static final String LOG_DELETED_SUCCESS = "Displayed successful delete event for task #%d.";
    public static final String LOG_UI_LIST = "The current list of task to be displayed is : ";
    public static final String LOG_LOGIC_LIST = "The list of task received from Logic is : ";
    public static final String LOG_IDINDEX_LIST = "The current mapping of On-screen ID and Storage ID is : ";
    public static final String LOG_DELETE_FAIL_NOID = "An non-existent ID was queried for delete.";
    public static final String LOG_RENAMED_SUCCESS = "Displayed successful rename event for task #%d.";
    public static final String LOG_RENAME_FAIL_NOID = "An non-existent ID was queried for rename.";
    public static final String LOG_RENAME_FAIL_NOCHANGE = "A renaming with no change was queried.";
    public static final String LOG_RENAME_FAIL_DUPLICATE = "A task was not renamed as a duplicate task exists.";
    public static final String LOG_RESCHED_SUCCESS = "Displayed successful reschedule event for task #%d.";
    public static final String LOG_RESCHE_FAIL_NOID = "An non-existent ID was queried for reschedule.";
    public static final String LOG_RESCHE_FAIL_NOCHANGE = "A rescheduling with no change was queried.";
    public static final String LOG_RESCHE_FAIL_DUPLICATE = "A task was not rescheduled as a duplicate task exists.";


    public static final char ARROW_UP = 0x25b2;
    public static final char ARROW_DOWN = 0x25bC;
    public static final String HELP_HOT_KEYS_DESC = "Navigate the tabs using hot keys";
    public static final String HELP_HOT_KEYS_COMMAND = "Incomplete <F1> / Overdue <F2> / Upcoming <F3> "
            + "/ All <F4> / Completed <F5> / Surprise <F6> / Help <F7>";
    public static final String HELP_PAGE_UP_DOWN_DESC = "Scroll the on-screen list";
    public static final String HELP_PAGE_UP_DOWN_COMMAND = "press your keyboard's page up/down button";
    public static final String HELP_UP_DOWN_DESC = "Scroll through your previous commands";
    public static final String HELP_UP_DOWN_COMMAND = "press your keyboard's up (" + ARROW_UP + ") / down ("
            + ARROW_DOWN + ") arrow";
    public static final String HELP_ADD_FLOATING_DESC = "Add a task with no time or date";
    public static final String HELP_ADD_FLOATING_COMMAND = "add <task description>";
    public static final String HELP_ADD_POINT_DESC = "Add a task to be done AT/ON a particular date and time";
    public static final String HELP_ADD_POINT_COMMAND = "add <task description> <at/on> <date-time>";
    public static final String HELP_ADD_DEADLINE_DESC = "Add a task to be done BY a certain date and time";
    public static final String HELP_ADD_DEADLINE_COMMAND = "add <task description> by <date-time>";
    public static final String HELP_ADD_EVENT_DESC = "Add a task with a start and end date";
    public static final String HELP_ADD_EVENT_COMMAND = "add <task description> from <date-time> to <date-time>";
    public static final String HELP_LIST_DESC = "See incomplete/overdue/upcoming/all/completed tasks";
    public static final String HELP_LIST_COMMAND = "list <incomplete/overdue/upcoming/all/completed>";
    public static final String HELP_RENAME_DESC = "Rename a task";
    public static final String HELP_RENAME_COMMAND = "rename <task number> <new task description>";
    public static final String HELP_RESCH_DESC = "Reschedule a task";
    public static final String HELP_RESCH_COMMAND = "reschedule <task number> <new task date-time>";
    public static final String HELP_REMOVE_TIME_DESC = "Remove date and time restrictions";
    public static final String HELP_REMOVE_TIME_COMMAND = "reschedule <task number>";
    public static final String HELP_DONE_DESC = "Mark task as completed";
    public static final String HELP_DONE_COMMAND = "mark <task number>";
    public static final String HELP_UNDONE_DESC = "Mark task as incomplete";
    public static final String HELP_UNDONE_COMMAND = "unmark <task number>";
    public static final String HELP_DELETE_DESC = "Delete a task";
    public static final String HELP_DELETE_COMMAND = "delete <task number(s)>";
    public static final String HELP_SEARCH_DESC = "Search through your tasks for specific keywords";
    public static final String HELP_SEARCH_COMMAND = "search <keyword(s)>";
    public static final String HELP_UNDO_DESC = "Undo your previous change";
    public static final String HELP_UNDO_COMMAND = "undo";
    public static final String HELP_CREATE_ALIAS_DESC = "Define your own alias (command)";
    public static final String HELP_CREATE_ALIAS_COMMAND = "alias <default command> <new alias>";
    public static final String HELP_DELETE_ALIAS_DESC = "Remove a previously defined alias";
    public static final String HELP_DELETE_ALIAS_COMMAND = "unalias <alias>";
    public static final String HELP_WILDCARD_DESC = "Let JFDI surprise you";
    public static final String HELP_WILDCARD_COMMAND = "surprise!";
    public static final String HELP_CHECK_DIR_DESC = "Check what the current directory is";
    public static final String HELP_CHECK_DIR_COMMAND = "directory";
    public static final String HELP_USE_DIR_DESC = "Use data from another directory";
    public static final String HELP_USE_DIR_COMMAND = "use <new directory>";
    public static final String HELP_MOVE_DIR_DESC = "Move existing program data to another directory";
    public static final String HELP_MOVE_DIR_COMMAND = "move <new directory>";
    public static final String HELP_EXIT_DESC = "GET OUT OF JFDI :(";
    public static final String HELP_EXIT_COMMAND = "quit";

    public static final String ITEM_ROW_INDEX = " #%d  ";
    public static final String ITEM_NO_TIMEDATE = "Anytime ~";
    public static final String ITEM_DEADLINE = "by %s";
    public static final String ITEM_POINT_TASK = "on %s";
    public static final String ITEM_EVENT_TASK = "from %s to %s";
}
