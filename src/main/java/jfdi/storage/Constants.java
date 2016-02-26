package jfdi.storage;

import java.time.Duration;
import java.time.LocalDateTime;

import java.util.Arrays;
import java.util.List;

import jfdi.storage.records.Alias;
import jfdi.storage.records.Task;

/**
 * This class file contains all the constants used within the Storage component.
 *
 * @author Thng Kai Yuan
 */
public class Constants {

    /**
     * General purpose constants
     */
    // Filenames
    public static final String FILENAME_TASK = "tasks.txt";
    public static final String FILENAME_ALIAS = "aliases.txt";
    public static final String[] FILENAME_ARRAY = {Constants.FILENAME_TASK, Constants.FILENAME_ALIAS};

    // File extensions
    public static final String EXTENSION_BACKUP = ".bak";

    // Empty stuff
    public static final String EMPTY_STRING = "";
    public static final String EMPTY_LIST_STRING = "[]";

    // Error messages
    public static final String MESSAGE_INVALID_PATH = "The directory %s cannot be used to store the program data.";
    public static final String MESSAGE_UNINITIALIZED_STORAGE = "Storage needs to be initialized first.";

    // Default charset
    public static final String CHARSET = "UTF-8";


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

    // Tasks
    public static final String TEST_TASK_DESCRIPTION = "my favourite description";
    public static final String TEST_TASK_TAG_1 = "tagged";
    public static final String TEST_TASK_TAG_2 = "tagged2";
    public static final LocalDateTime TEST_TASK_STARTDATETIME = LocalDateTime.MIN;
    public static final LocalDateTime TEST_TASK_ENDDATETIME = LocalDateTime.MAX;
    public static final Duration TEST_TASK_REMINDER_DURATION_1 = Duration.ZERO.plusDays(1);
    public static final Duration TEST_TASK_REMINDER_DURATION_2 = Duration.ZERO.plusDays(2);


    /**
     * Private variables
     */
    // List of records
    private static final List<Class<?>> RECORDS = Arrays.asList(Task.class, Alias.class);


    /**
     * @return an immutable array of record classes
     */
    public static Class<?>[] getRecords() {
        return RECORDS.toArray(new Class<?>[RECORDS.size()]);
    }

}
