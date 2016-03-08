package jfdi.storage;

import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.ArrayList;

import jfdi.storage.exceptions.ExistingFilesFoundException;
import jfdi.storage.exceptions.FilePathPair;

/**
 * This class manages all the object databases in the Storage
 * component. All databases (e.g. TaskDb, AliasDb) are expected to implement
 * the following static methods that will be called via reflection:
 * + load()
 * + persist()
 * + getFilePath()
 * + setFilePath(String)
 *
 * @author Thng Kai Yuan
 */
public class DatabaseManager {

    /*
     * Public APIs
     */

    /**
     * This method sets the file path of each database accordingly, using
     * storageFolderPath as the root directory of all data.
     *
     * @param storageFolderPath
     *            the root directory where all data will be stored
     */
    public static void setAllFilePaths(String storageFolderPath) {
        for (Class<?> database : Constants.getDatabases()) {
            setDatabaseFilePath(storageFolderPath, database);
        }
    }

    /**
     * This method persists all databases to disk.
     */
    public static void persistAll() {
        for (Class<?> database : Constants.getDatabases()) {
            persist(database);
        }
    }

    /**
     * This method loads/refreshes all databases based on data contained within
     * the data file defined by the file path of each record.
     *
     * @throws ExistingFilesFoundException
     *             if unrecognized files were replaced (with backups made)
     */
    public static void loadAllDatabases() throws ExistingFilesFoundException {
        ArrayList<FilePathPair> replacedFiles = new ArrayList<FilePathPair>();
        FilePathPair filePathPair = null;

        for (Class<?> database : Constants.getDatabases()) {
            filePathPair = loadDatabase(database);
            if (filePathPair != null) {
                replacedFiles.add(filePathPair);
            }
        }

        if (!replacedFiles.isEmpty()) {
            throw new ExistingFilesFoundException(replacedFiles);
        }
    }

    /**
     * @return an ArrayList of Paths where the existing data for each database is
     *         stored.
     */
    public static ArrayList<Path> getAllFilePaths() {
        ArrayList<Path> filePaths = new ArrayList<Path>();

        for (Class<?> database : Constants.getDatabases()) {
            Path filePath = getDatabaseFilePath(database);
            filePaths.add(filePath);
        }

        return filePaths;
    }


    /*
     * Private helper methods
     */

    /**
     * This method executes the getFilePath method on the given database, that is,
     * it calls (something like) database.getFilePath().
     *
     * @param database
     *            the database which contains the getFilePath method
     * @return the Path of the existing data file for the database
     */
    private static Path getDatabaseFilePath(Class<?> database) {
        Path filePath = null;

        try {
            Method method = database.getMethod("getFilePath");
            filePath = (Path) method.invoke(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return filePath;
    }

    /**
     * This method executes the setFilePath method on the given database, that is,
     * it calls (something like) database.setFilePath(storageFolderPath).
     *
     * @param storageFolderPath
     *            the parameter for the setFilePath method
     * @param database
     *            the database which contains the setFilePath method
     */
    private static void setDatabaseFilePath(String storageFolderPath, Class<?> database) {
        try {
            Method method = database.getMethod("setFilePath", String.class);
            method.invoke(null, storageFolderPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method executes the load method on the given database, that is, it
     * calls (something like) database.load() and returns the return value of the
     * method call.
     *
     * @param database
     *            the database which contains the load method
     * @return FilePathPair if a file was replaced, null otherwise
     */
    private static FilePathPair loadDatabase(Class<?> database) {
        FilePathPair filePathPair = null;

        try {
            Method method = database.getMethod("load");
            filePathPair = (FilePathPair) method.invoke(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return filePathPair;
    }

    /**
     * This method executes the persist method on the given database, that is,
     * it calls (something like) database.persist().
     *
     * @param database
     *            the record which contains the persist method
     */
    private static void persist(Class<?> database) {
        try {
            Method method = database.getMethod("persist");
            method.invoke(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
