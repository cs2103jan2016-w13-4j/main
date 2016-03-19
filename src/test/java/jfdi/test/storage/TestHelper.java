package jfdi.test.storage;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import jfdi.storage.Constants;
import jfdi.storage.FileManager;
import jfdi.storage.apis.MainStorage;

import org.apache.commons.io.FileUtils;

public class TestHelper {

    /**
     * This method ensures that two given collections have the same elements.
     *
     * @param collection1
     *            the first collection to be compared
     * @param collection2
     *            the second collection to be compared
     * @return boolean indicating if the two collections have the same elements
     */
    public static <T> boolean hasSameElements(Collection<T> collection1, Collection<T> collection2) {
        HashSet<T> set1 = new HashSet<T>(collection1);
        HashSet<T> set2 = new HashSet<T>(collection2);
        return set1.equals(set2);
    }

    /**
     * This method returns the path to the data directory within the storage
     * directory.
     *
     * @param storageDirectory
     *            the folder which should store the user data
     * @return the path to the data directory within the storage directory
     */
    public static String getDataDirectory(String storageDirectory) {
        return MainStorage.getInstance().getDataDirectory(storageDirectory);
    }

    /**
     * This method creates a single valid task with the task's description set
     * as the first test task description.
     *
     * @param directoryPath
     *            the directory that holds the program data
     */
    public static void createValidTaskFile(String directoryPath) {
        String taskJson = "[{\"id\": 1,\"description\": \"" + Constants.TEST_TASK_DESCRIPTION_1
                + "\",\"tags\": [],\"reminders\": [],\"isCompleted\": false}]";
        createTaskFileWith(directoryPath, taskJson);
    }

    /**
     * This method creates a single invalid task with the task's description set
     * to null.
     *
     * @param directoryPath
     *            the directory that holds the program data
     */
    public static void createInvalidTaskFile(String directoryPath) {
        String taskJson = "[{\"id\": 1,\"tags\": [],\"reminders\": [],\"isCompleted\": false}]";
        createTaskFileWith(directoryPath, taskJson);
    }

    /**
     * This method creates a single invalid alias with the alias's command set
     * to null
     *
     * @param directoryPath
     *            the directory that holds the program data
     */
    public static void createInvalidAliasFile(String directoryPath) {
        String taskJson = "[{\"alias\": \"" + Constants.TEST_ALIAS + "\"}]";
        createAliasFileWith(directoryPath, taskJson);
    }

    /**
     * This method creates a task file with the given data in the given
     * directory path that stores the program data.
     *
     * @param directoryPath
     *            the directory that stores the program data
     * @param data
     *            the content that is to be written inside the task file
     */
    public static void createTaskFileWith(String directoryPath, String data) {
        String dataDirectory = TestHelper.getDataDirectory(directoryPath);
        Path dataPath = Paths.get(dataDirectory, Constants.FILENAME_TASK);
        FileManager.writeToFile(data, dataPath);
    }

    /**
     * This method creates an alias file with the given data in the given
     * directory path that stores the program data.
     *
     * @param directoryPath
     *            the directory that stores the program data
     * @param data
     *            the content that is to be written inside the task file
     */
    public static void createAliasFileWith(String directoryPath, String data) {
        String dataDirectory = TestHelper.getDataDirectory(directoryPath);
        Path dataPath = Paths.get(dataDirectory, Constants.FILENAME_ALIAS);
        FileManager.writeToFile(data, dataPath);
    }

    /**
     * This method creates valid data files in directoryPath
     *
     * @param directoryPath
     *            the directory to create the data files in
     */
    public static void createValidDataFiles(String directoryPath) {
        createDataFilesWith(directoryPath, Constants.EMPTY_LIST_STRING);
    }

    /**
     * This method creates invalid data files in directoryPath.
     *
     * @param directoryPath
     *            the directory to create the data files in
     */
    public static void createInvalidDataFiles(String directoryPath) {
        createDataFilesWith(directoryPath, Constants.EMPTY_STRING);
    }

    /**
     * This method creates data files in the specified directoryPath with the
     * given data written into the data files.
     *
     * @param directoryPath
     *            the path to the directory in which we create the data files
     * @param data
     *            the content that is to be written to the data files
     */
    public static void createDataFilesWith(String directoryPath, String data) {
        File parentDirectory = new File(directoryPath);
        parentDirectory.mkdirs();

        Path dataPath;
        String dataDirectory = TestHelper.getDataDirectory(directoryPath);
        for (String dataFilename : Constants.FILENAME_DATA_ARRAY) {
            dataPath = Paths.get(dataDirectory, dataFilename);
            FileManager.writeToFile(data, dataPath);
        }
    }

    /**
     * This method reverts the preference file to its original form after using
     * it in the tests.
     *
     * @param originalPreference
     *            the original storage directory path
     */
    public static void revertOriginalPreference(MainStorage mainStorage, String originalPreference) {
        if (originalPreference == null) {
            FileUtils.deleteQuietly(Constants.PATH_PREFERENCE_FILE.toFile());
            return;
        }
        mainStorage.setPreferredDirectory(originalPreference);
    }

    /**
     * This method returns an ArrayList of data files that exist in the given directory.
     *
     * @param directory
     *          the directory which contains data files
     * @return an ArrayList of data files that exist in the given directory
     */
    private static ArrayList<File> getDataFiles(String directory) {
        ArrayList<File> dataFiles = new ArrayList<File>();
        Path dataFilePath = null;
        File dataFile = null;

        for (String filename : Constants.FILENAME_DATA_ARRAY) {
            dataFilePath = Paths.get(directory, filename);
            dataFile = dataFilePath.toFile();
            if (dataFile.exists()) {
                dataFiles.add(dataFile);
            }
        }

        return dataFiles;
    }

    public static HashMap<String, Long> getDataFileChecksums(String storageDirectory) {
        try {
            HashMap<String, Long> checksums = new HashMap<String, Long>();
            String dataDirectory = getDataDirectory(storageDirectory);
            ArrayList<File> dataFiles = getDataFiles(dataDirectory);
            for (File dataFile : dataFiles) {
                checksums.put(dataFile.getName(), FileUtils.checksumCRC32(dataFile));
            }
            return checksums;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean hasDataFileChecksums(String storageDirectory, HashMap<String, Long> checksums) {
        HashMap<String, Long> directoryChecksums = getDataFileChecksums(storageDirectory);
        return checksums.equals(directoryChecksums);
    }
}
