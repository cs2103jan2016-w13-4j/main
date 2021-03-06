//@@author A0121621Y

package jfdi.storage;

import java.nio.file.Path;

import jfdi.storage.exceptions.FilePathPair;

/**
 * This interface defines the methods that an entity database should implement
 * in order for the database to be managed by DatabaseManager appropriately.
 *
 * @author Thng Kai Yuan
 *
 */
public interface IDatabase {

    /**
     * This method loads the data currently stored on disk at the location given
     * by filePath.
     *
     * @return a FilePathPair if the existing file cannot be read and was
     *         renamed
     */
    FilePathPair load();

    /**
     * This method persists all existing data to the file system.
     */
    void persist();

    /**
     * @return the path of the folder storing the database file
     */
    Path getFilePath();

    /**
     * This method sets the path of the folder used to store the database file.
     *
     * @param absoluteFolderPath
     *            the directory that should contain the database file
     */
    void setFilePath(String path);

    /**
     * Resets the database.
     */
    void reset();

}
