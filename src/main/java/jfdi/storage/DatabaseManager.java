package jfdi.storage;

import java.nio.file.Path;
import java.util.ArrayList;

import jfdi.storage.exceptions.ExistingFilesFoundException;
import jfdi.storage.exceptions.FilePathPair;

/**
 * This class manages all the databases in the Storage component.
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
        for (IDatabase database : Constants.DATABASES) {
            database.setFilePath(storageFolderPath);
        }
    }

    /**
     * This method persists all databases to disk.
     */
    public static void persistAll() {
        for (IDatabase database : Constants.DATABASES) {
            database.persist();
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

        for (IDatabase database : Constants.DATABASES) {
            filePathPair = database.load();
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

        for (IDatabase database : Constants.DATABASES) {
            Path filePath = database.getFilePath();
            filePaths.add(filePath);
        }

        return filePaths;
    }

}
