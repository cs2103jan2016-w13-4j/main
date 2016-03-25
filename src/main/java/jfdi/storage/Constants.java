//@@author A0121621Y

package jfdi.storage;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import jfdi.storage.apis.AliasDb;
import jfdi.storage.apis.TaskDb;

/**
 * This class file contains all the constants used within the Storage component.
 *
 * @author Thng Kai Yuan
 */
public class Constants {

    /**
     * General purpose constants
     */
    // Database filenames
    public static final String FILENAME_TASK = "tasks.txt";
    public static final String FILENAME_ALIAS = "aliases.txt";
    public static final String[] FILENAME_DATA_ARRAY = {Constants.FILENAME_TASK, Constants.FILENAME_ALIAS};

    // Last used directory filenames and paths
    public static final String FILENAME_DIRECTORY = "directory.txt";
    public static final String FILENAME_DATA_DIRECTORY = ".jfdi_user_data";
    public static final String PATH_DEFAULT_DIRECTORY = ".";
    public static final Path PATH_PREFERENCE_FILE = Paths.get(PATH_DEFAULT_DIRECTORY,
            FILENAME_DATA_DIRECTORY, FILENAME_DIRECTORY);

    // Logging filename and path
    public static final String FILENAME_LOG = "log.txt";
    public static final String FILENAME_LOG_DIRECTORY = ".jfdi_logs";
    public static final Path PATH_LOG_FILE = Paths.get(PATH_DEFAULT_DIRECTORY, FILENAME_LOG_DIRECTORY, FILENAME_LOG);

    // File extensions
    public static final String EXTENSION_BACKUP = ".bak";

    // Empty stuff
    public static final String EMPTY_STRING = "";
    public static final String EMPTY_LIST_STRING = "[]";

    // Error messages
    public static final String MESSAGE_INVALID_PATH = "The directory %s cannot be used to store the program data.";
    public static final String MESSAGE_MISSING_DESCRIPTION = "This task is missing a description.";
    public static final String MESSAGE_INVALID_DATETIME = "Start date and time cannot be after end date and time.";

    // Logging messages
    public static final String MESSAGE_LOG_SET_DIRECTORY = "Storage directory has been set to '%s'.";
    public static final String MESSAGE_LOG_CREATE_TASK = "A new task with ID %d was created.";
    public static final String MESSAGE_LOG_UPDATE_TASK = "Task #%d was updated.";
    public static final String MESSAGE_LOG_DELETE_TASK = "Task #%d was deleted.";
    public static final String MESSAGE_LOG_RECOVER_TASK = "Task #%d was recovered.";

    // Default charset
    public static final String CHARSET = "UTF-8";

    // All database instances
    public static final IDatabase[] DATABASES = {TaskDb.getInstance(), AliasDb.getInstance()};


    /**
     * Test-specific constants
     */
    // Files
    public static final String TEST_DIRECTORY_PREFIX = "CS2103-JFDI";
    public static final String TEST_DIRECTORY_NAME = "Data";
    public static final String TEST_SUBDIRECTORY_NAME = "Data2";
    public static final String TEST_FILE_NAME = "test.txt";
    public static final String TEST_FILE_DATA = "hello world!";

    // Aliases
    public static final String TEST_ALIAS = "somealias";
    public static final String TEST_COMMAND = "somecommand";
    public static final String TEST_ALIAS_2 = "somealias2";
    public static final String TEST_COMMAND_2 = "somecommand2";
    public static final String TEST_COMMAND_REGEX = "(?i)^(" + TEST_COMMAND + ")|"
            + "(?i)^(" + TEST_COMMAND_2 + ")";

    // Tasks
    public static final String TEST_TASK_DESCRIPTION_1 = "my favourite description";
    public static final String TEST_TASK_DESCRIPTION_2 = "my favourite description too";
    public static final LocalDateTime TEST_TASK_STARTDATETIME = LocalDateTime.MIN;
    public static final LocalDateTime TEST_TASK_ENDDATETIME = LocalDateTime.MAX;

}
