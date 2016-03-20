package jfdi.storage;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import jfdi.storage.exceptions.FilesReplacedException;
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
        assert storageFolderPath != null;
        for (IDatabase database : Constants.DATABASES) {
            database.setFilePath(storageFolderPath);
        }
    }

    /**
     * This method persists all databases to disk.
     */
    public static void persistAll() {
        Arrays.stream(Constants.DATABASES).forEach(IDatabase::persist);
    }

    /**
     * This method loads/refreshes all databases based on data contained within
     * the data file defined by the file path of each record.
     *
     * @throws FilesReplacedException
     *             if unrecognized files were moved
     */
    public static void loadAllDatabases() throws FilesReplacedException {
        ArrayList<FilePathPair> movedFiles = new ArrayList<FilePathPair>();
        Arrays.stream(Constants.DATABASES)
            .map(IDatabase::load)
            .filter(Objects::nonNull)
            .forEach(movedFiles::add);

        if (!movedFiles.isEmpty()) {
            throw new FilesReplacedException(movedFiles);
        }
    }

    /**
     * This method returns an ArrayList of Paths to each data file.
     *
     * @return an ArrayList of Paths where the existing data for each database
     *         is stored
     */
    public static ArrayList<Path> getAllFilePaths() {
        ArrayList<Path> filePaths = new ArrayList<Path>();
        Arrays.stream(Constants.DATABASES)
            .map(IDatabase::getFilePath)
            .forEach(filePaths::add);

        return filePaths;
    }

}
