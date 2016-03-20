package jfdi.ui;

public class Constants {

    public static final String UI_MESSAGE_INIT = "Initializing UI...";
    public static final String UI_MESSAGE_INITED = "Initialization Completed!";
    public static final String UI_MESSAGE_GREETING = "J.F.D.I. : Hello Jim! Nice to see you again! :)";
    public static final String UI_MESSAGE_WELCOME = "\nJ.F.D.I. : What can I do for you?";
    public static final String UI_MESSAGE_USERCMD = "You said: %1$s\n";
    public static final String UI_MESSAGE_RESPONSE = "J.F.D.I. : %1$s\n";
    public static final String UI_MESSAGE_WARNING = "Warning: %1$s\n";
    public static final String UI_MESSAGE_ERROR = "Error: %1$s\n";
    public static final String UI_MESSAGE_QUIT = "Bye Bye! See you next time! :)\n";

    public static final String URL_ROOT_PATH = "/ui/RootLayout.fxml";
    public static final String URL_LIST_PATH = "/ui/ListLayout.fxml";
    public static final String URL_HELP_PATH = "/ui/HelpItem.fxml";
    public static final String URL_ITEM_PATH = "/ui/ListItem.fxml";

    public static final String CTRL_CMD_PROMPT_TEXT = "(Hey Jim! Please let me know what I can do for you!)";
    public static final String CTRL_CMD_SHOWLIST = "list";
    public static final int OVERLAY_FADE_IN_MILLISECONDS = 200;


    public enum MsgType {
        SUCCESS, WARNING, ERROR, EXIT
    }

    public static final String CMD_ERROR_CANT_ADD_UNKNOWN = "Some stupid error occurred. Cannot add task!";
    public static final String CMD_ERROR_CANT_ADD_EMPTY = "Cannot add an empty task!";
    public static final String CMD_ERROR_CANT_ALIAS_INVALID = "- %s - is an invalid alias for - %s -";
    public static final String CMD_ERROR_CANT_ALIAS_DUPLICATED = " - %s - is an duplicated alias.";
    public static final String CMD_ERROR_CANT_ALIAS_UNKNOWN =
            "Some stupid error occurred. Cannot create alias for - %s -!";
    public static final String CMD_ERROR_CANT_DELETE_NO_ID = "Some stupid error occurred. Cannot delete task(s)!";
    public static final String CMD_ERROR_CANT_DELETE_UNKNOWN = "Cannot delete task. The ID #%d does not exist!";
    public static final String CMD_ERROR_CANT_MARK_NO_ID = "Cannot mark task as completed. The ID #%d does not exist!!";
    public static final String CMD_ERROR_CANT_MARK_UNKNOWN = "Some stupid error occurred. Cannot mark task(s) as done!";
    public static final String CMD_ERROR_CANT_RENAME_UNKNOWN = "Some stupid error occurred. Cannot rename task!";
    public static final String CMD_ERROR_CANT_RENAME_NO_ID = "Cannot rename task. The ID #%d does not exist!";
    public static final String CMD_ERROR_CANT_RENAME_NO_CHANGES = "No difference between new and old name - %s -!";
    public static final String CMD_ERROR_CANT_RESCHEDULE_UNKNOWN = "Some error occurred. Cannot reschedule task!";
    public static final String CMD_ERROR_CANT_RESCHEDULE_NO_ID = "Cannot reschedule task. The ID #%d does not exist!";
    public static final String CMD_ERROR_CANT_RESCHEDULE_NO_CHANGES = "No difference between new and old schedule - ";
    public static final String CMD_ERROR_CANT_UNALIAS_UNKNOWN = "Some error occurred. Cannot remove alias - %s -!";
    public static final String CMD_ERROR_CANT_UNALIAS_NO_ALIAS =
            "Cannot remove alias. The alias - %s - does not exist!";
    public static final String CMD_ERROR_CANT_UNMARK_NO_ID = "Cannot unmark task. The ID #%d does not exist!!";
    public static final String CMD_ERROR_CANT_UNMARK_UNKNOWN = "Some stupid error occurred. Cannot unmark task(s)!";

    public static final String CMD_SUCCESS_ADDED = "Task #%d - %s added! :)";
    public static final String CMD_SUCCESS_ALIAS = "Alias - %s - is created for command - %s -! :)";
    public static final String CMD_SUCCESS_DELETED = "Task(s) deleted! :)";
    public static final String CMD_SUCCESS_LISTED = "Here is your requested list! :)";
    public static final String CMD_SUCCESS_MARKED = "Task(s) completed! Good job! :)";
    public static final String CMD_SUCCESS_RENAMED = "Task #%d renamed to - %s -! :)";
    public static final String CMD_SUCCESS_RESCHEDULED = "Task #%d rescheduled! :)";
    public static final String CMD_SUCCESS_SEARCH = "Here are your search results! :)";
    public static final String CMD_SUCCESS_SHOWDIRECTORY = "Your current directory is : %s ! :)";
    public static final String CMD_SUCCESS_UNALIAS = "Alias - %s - is removed! :)";
    public static final String CMD_SUCCESS_UNMARKED = "Task(s) marked as imcomplete! :)";

    public static final String CMD_WARNING_DONTKNOW = "Sorry, I do not understand what you mean by \"%s\" :(";

    public static final String LOG_FXML_PATH = "Loaded %s from %s'.";
    public static final String LOG_SUCCESS_LISTED = "List requested.";
    public static final String LOG_USER_EXIT = "Exited from JFDI.";
    public static final String LOG_INVALID_COMMAND = "Invalid command detected.";
    public static final String LOG_ADDED_SUCCESS = "Displayed successful add event for task #%d.";
    public static final String LOG_ADD_FAIL_UNKNOWN = "Unknown error detected for adding.";
    public static final String LOG_ADD_FAIL_EMPTY = "An task failed to add due to empty description.";
    public static final String LOG_DELETED_SUCCESS = "Displayed successful delete event for task #%d.";
    public static final String LOG_UI_LIST = "The current list of task to be displayed is : ";
    public static final String LOG_LOGIC_LIST = "The list of task received from Logic is : ";
    public static final String LOG_IDINDEX_LIST = "The current mapping of On-screen ID and Storage ID is : ";
    public static final String LOG_DELETE_FAIL_UNKNOWN = "Unknown error detected for deleting.";
    public static final String LOG_DELETE_FAIL_NOID = "An non-existent ID was queried for delete.";
    public static final String LOG_RENAMED_SUCCESS = "Displayed successful rename event for task #%d.";
    public static final String LOG_RENAME_FAIL_UNKNOWN = "Unknown error detected for renaming.";
    public static final String LOG_RENAME_FAIL_NOID = "An non-existent ID was queried for rename.";
    public static final String LOG_RENAME_FAIL_NOCHANGE = "A renaming with no change was queried.";
    public static final String LOG_RESCHED_SUCCESS = "Displayed successful reschedule event for task #%d.";
    public static final String LOG_RESCHE_FAIL_UNKNOWN = "Unknown error detected for reschduling.";
    public static final String LOG_RESCHE_FAIL_NOID = "An non-existent ID was queried for reschedule.";
    public static final String LOG_RESCHE_FAIL_NOCHANGE = "A rescheduling with no change was queried.";

    public static final String HELP_OVERLAY_TITLE = "JFDI TO THE RESCUE!";
    public static final String HELP_OVERLAY_ICON = "\uf05a"; // SUB IN ACTUAL ADDRESS
    public static final String HELP_ADD_FLOATING_DESC = "Add a task with no specific time or date";
    public static final String HELP_ADD_FLOATING_COMMAND = "add <task description>";
    public static final String HELP_ADD_POINT_DESC = "Add a task to be done AT/ON a particular date and time";
    public static final String HELP_ADD_POINT_COMMAND = "add <task description> <at/on> <datetime>";
    public static final String HELP_ADD_DEADLINE_DESC = "Add a task to be done BY a certain date and time";
    public static final String HELP_ADD_DEADLINE_COMMAND = "add <task description> by <datetime>";
    public static final String HELP_ADD_EVENT_DESC = "Add a task with a start and end date";
    public static final String HELP_ADD_EVENT_COMMAND = "add <task description> from <datetime> to <datetime>";
    public static final String HELP_LIST_INCOMPLETE_DESC = "See incomplete tasks";
    public static final String HELP_LIST_INCOMPLETE_COMMAND = "list";
    public static final String HELP_LIST_COMPLETE_DESC = "See completed tasks";
    public static final String HELP_LIST_COMPLETE_COMMAND = "list completed";
    public static final String HELP_LIST_ALL_DESC = "See all tasks";
    public static final String HELP_LIST_ALL_COMMAND = "list all";
    public static final String HELP_RENAME_DESC = "Rename a task";
    public static final String HELP_RENAME_COMMAND = "rename  <task number> <new task description>";
    public static final String HELP_RESCH_DESC = "Reschedule a task";
    public static final String HELP_RESCH_COMMAND = "reschedule <task number> <new task date and time>";
    public static final String HELP_REMOVE_TIME_DESC = "Remove date and time restrictions";
    public static final String HELP_REMOVE_TIME_COMMAND = "reschedule <task number>";
    public static final String HELP_DONE_DESC = "Mark task as completed";
    public static final String HELP_DONE_COMMAND = "mark <task number>";
    public static final String HELP_UNDONE_DESC = "Mark task as incomplete again";
    public static final String HELP_UNDONE_COMMAND = "unmark <task number>";
    public static final String HELP_DELETE_DESC = "Delete a task";
    public static final String HELP_DELETE_COMMAND = "delete <task number(s)>";
    public static final String HELP_SEARCH_DESC = "Search through the tasks for specific keywords";
    public static final String HELP_SEARCH_COMMAND = "search <keyword(s)>";
    public static final String HELP_REMINDER_DESC = "add custom reminders to task with a start date and time";
    public static final String HELP_REMINDER_COMMAND = "remind me <duration> before <task number(s)>";
    public static final String HELP_UNDO_DESC = "Undo previous action";
    public static final String HELP_UNDO_COMMAND = "undo";
    public static final String HELP_CREATE_ALIAS_DESC = "Define your own aliases";
    public static final String HELP_CREATE_ALIAS_COMMAND = "alias <default command> as <new alias>";
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
    public static final String HELP_UP_DOWN_DESC = "Scroll through your previous commands";
    public static final String HELP_UP_DOWN_COMMAND = "↑/↓";
    public static final String HELP_EXIT_DESC = "GET OUT OF JFDI";
    public static final String HELP_EXIT_COMMAND = "quit";

    public static final String ITEM_ROW_INDEX = " #%d  ";
    public static final String ITEM_NO_TIMEDATE = "No specified time and date.";
    public static final String ITEM_DEADLINE = "by %s";
    public static final String ITEM_POINT_TASK = "at %s";
    public static final String ITEM_EVENT_TASK = "from %s to %s";
}
