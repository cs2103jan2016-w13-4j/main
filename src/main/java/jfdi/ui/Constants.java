package jfdi.ui;

public class Constants {

    public static final String UI_MESSAGE_INIT = "Initializing UI...";
    public static final String UI_MESSAGE_INITED = "Initialization Completed!";
    public static final String UI_MESSAGE_GREETING = "\n\nJ.F.D.I. : Hello Jim! Nice to see you again! :)\n";
    public static final String UI_MESSAGE_WELCOME = "J.F.D.I. : What can I do for you?\n";
    public static final String UI_MESSAGE_USERCMD = "You said: %1$s\n";
    public static final String UI_MESSAGE_RESPONSE = "J.F.D.I. : %1$s\n";
    public static final String UI_MESSAGE_WARNING = "Warning: %1$s\n";
    public static final String UI_MESSAGE_ERROR = "Error: %1$s\n";
    public static final String UI_MESSAGE_QUIT = "Bye Bye! See you next time! :)\n";

    public static final String MAIN_ROOT_PATH = "/ui/RootLayout.fxml";
    public static final String MAIN_LIST_PATH = "/ui/ListLayout.fxml";

    public static final String CTRL_CMD_PROMPT_TEXT = "(Hey Jim! Please let me know what I can do for you!)";
    public static final String CTRL_CMD_SHOWLIST = "list";

    public enum MsgType {
        SUCCESS, WARNING, ERROR, EXIT
    }

    public static final String CMD_ERROR_CANT_ADD_UNKNOWN = "Some stupid error occurred. Cannot add task!";
    public static final String CMD_ERROR_CANT_ADD_EMPTY = "Cannot add an empty task!";
    public static final String CMD_ERROR_CANT_DELETE = "Some stupid error occurred. Cannot delete task!";
    public static final String CMD_ERROR_CANT_RENAME_UNKNOWN = "Some stupid error occurred. Cannot rename task!";
    public static final String CMD_ERROR_CANT_RENAME_NO_ID = "Cannot rename task. The ID #%d does not exist!";
    public static final String CMD_ERROR_CANT_RENAME_NO_CHANGES = "No difference between new and old name - %s -!";
    public static final String CMD_ERROR_CANT_RESCHEDULE_UNKNOWN = "Some error occurred. Cannot reschedule task!";
    public static final String CMD_ERROR_CANT_RESCHEDULE_NO_ID = "Cannot reschedule task. The ID #%d does not exist!";
    public static final String CMD_ERROR_CANT_RESCHEDULE_NO_CHANGES = "No difference between new and old schedule - ";

    public static final String CMD_SUCCESS_LISTED = "Here is your requested list! :)";
    public static final String CMD_SUCCESS_ADDED = "Task #%d - %s added! :)";
    public static final String CMD_SUCCESS_DELETED = "Task #%d deleted! :)";
    public static final String CMD_SUCCESS_RENAMED = "Task #%d renamed to - %s -! :)";
    public static final String CMD_SUCCESS_RESCHEDULED = "Task #%d rescheduled! :)";

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


}
