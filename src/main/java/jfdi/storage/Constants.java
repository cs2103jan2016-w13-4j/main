package jfdi.storage;

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

    // Filenames
    public static final String FILENAME_TASK = "tasks.txt";
    public static final String FILENAME_ALIAS = "aliases.txt";
    public static final String[] ARRAY_FILENAMES = {Constants.FILENAME_TASK, Constants.FILENAME_ALIAS};

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

    // Test-specific constants
    public static final String TEST_DIRECTORY_PREFIX = "CS2103-JFDI";
    public static final String TEST_DIRECTORY_NAME = "Data";
    public static final String TEST_SUBDIRECTORY_NAME = "Data2";
    public static final String TEST_FILE_NAME = "test.txt";
    public static final String TEST_FILE_DATA = "hello world!";

    // List of records
    private static final List<Class<?>> RECORDS = Arrays.asList(Task.class, Alias.class);

    /**
     * @return an immutable array of record classes
     */
    public static Class<?>[] getRecords() {
        return RECORDS.toArray(new Class<?>[RECORDS.size()]);
    }

}
