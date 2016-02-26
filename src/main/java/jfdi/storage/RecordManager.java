package jfdi.storage;

import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.ArrayList;

import jfdi.storage.exceptions.ExistingFilesFoundException;
import jfdi.storage.exceptions.FilePathPair;

/**
 * This class manages operations related to all records in the Storage
 * component. All records (e.g. Task, Alias) are expected to implement
 * the following static methods that will be called via reflection:
 *
 * + load()
 * + getFilePath()
 * + setFilePath(String)
 *
 * @author Thng Kai Yuan
 */
public class RecordManager {

    /*
     * Public APIs
     */

    /**
     * This method sets the filepath of each record accordingly, using
     * storageFolderPath as the root directory of all data.
     *
     * @param storageFolderPath
     *            the root directory where all data will be stored
     */
    public static void setAllFilePaths(String storageFolderPath) {
        for (Class<?> record : Constants.getRecords()) {
            setRecordFilePath(storageFolderPath, record);
        }
    }

    /**
     * This method loads/refreshes all records based on data contained within
     * the data file defined by the filepath of each record.
     *
     * @throws ExistingFilesFoundException
     *             if unrecognized files were replaced (with backups made)
     */
    public static void loadAllRecords() throws ExistingFilesFoundException {
        ArrayList<FilePathPair> replacedFiles = new ArrayList<FilePathPair>();
        FilePathPair filePathPair = null;

        for (Class<?> record : Constants.getRecords()) {
            filePathPair = loadRecord(record);
            if (filePathPair != null) {
                replacedFiles.add(filePathPair);
            }
        }

        if (!replacedFiles.isEmpty()) {
            throw new ExistingFilesFoundException(replacedFiles);
        }
    }

    /**
     * @return an ArrayList of Paths where the existing data for each record is
     *         stored.
     */
    public static ArrayList<Path> getAllFilePaths() {
        ArrayList<Path> filePaths = new ArrayList<Path>();

        for (Class<?> record : Constants.getRecords()) {
            Path filePath = getRecordFilePath(record);
            filePaths.add(filePath);
        }

        return filePaths;
    }


    /*
     * Private helper methods
     */

    /**
     * This method executes the getFilePath method on the given record, that is,
     * it calls (something like) record.getFilePath().
     *
     * @param record
     *            the record which contains the getFilePath method
     * @return the Path of the existing data file for the record
     */
    private static Path getRecordFilePath(Class<?> record) {
        Path filePath = null;

        try {
            Method method = record.getMethod("getFilePath");
            filePath = (Path) method.invoke(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return filePath;
    }

    /**
     * This method executes the setFilePath method on the given record, that is,
     * it calls (something like) record.setFilePath(storageFolderPath).
     *
     * @param storageFolderPath
     *            the parameter for the setFilePath method
     * @param record
     *            the record which contains the setFilePath method
     */
    private static void setRecordFilePath(String storageFolderPath, Class<?> record) {
        try {
            Method method = record.getMethod("setFilePath", String.class);
            method.invoke(null, storageFolderPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method executes the load method on the given record, that is, it
     * calls (something like) record.load() and returns the return value of the
     * method call.
     *
     * @param record
     *            the record which contains the load method
     * @return FilePathPair if a file was replaced, null otherwise
     */
    private static FilePathPair loadRecord(Class<?> record) {
        FilePathPair filePathPair = null;

        try {
            Method method = record.getMethod("load");
            filePathPair = (FilePathPair) method.invoke(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return filePathPair;
    }

}
