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

    public static final String LOG_SET_DIRECTORY = "Storage directory has been set to '%s'.";
    public static final String LOG_CREATE_TASK = "A new task with ID %d was created.";
    public static final String LOG_UPDATE_TASK = "Task #%d was updated.";
    public static final String LOG_DELETE_TASK = "Task #%d was deleted.";
}
