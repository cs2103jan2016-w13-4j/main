//@@author A0121621Y

package jfdi.storage.apis;

import jfdi.storage.exceptions.FilesReplacedException;
import jfdi.storage.exceptions.InvalidFilePathException;

/**
 * The Storage interface deals with all the file path operations within the
 * program.
 *
 * @author Thng Kai Yuan
 */
public interface IStorage {

    /**
     * This method initializes storage with data from the folder used in the
     * previous program run. If no existing data files are found, Storage is
     * initialized with no data. If an invalid data files are found at the given
     * location, the invalid files will be renamed and kept as a backup while a
     * new file will overwrite the existing invalid file.
     *
     * @throws FilesReplacedException
     *             if existing unrecognized data files are found and replaced
     *             (with backups made) in the given storageFolderPath
     * @throws InvalidFilePathException
     *             if the given path is invalid
     */
    void initialize() throws FilesReplacedException, InvalidFilePathException;

    /**
     * This method can only be executed after storage is initialized. It
     * attempts to load data from the given directory and if the existing data
     * is not readable, the files are replaced with blank data files. This new
     * storage path is then remembered and used during the next program run.
     *
     * @param newStorageFolderPath
     *            the path to the directory that contains the JFDI user data
     * @throws InvalidFilePathException
     *             if the given path is invalid
     * @throws FilesReplacedException
     *             if invalid files were replaced in the process
     */
    void use(String newStorageFolderPath) throws InvalidFilePathException, FilesReplacedException;

    /**
     * This method can only be executed after storage is initialized. It saves
     * and transfers existing data in the current storage folder path into the
     * new storage folder path. The old files are then deleted upon a successful
     * transfer. This new storage path is then stored as the storage path that
     * is to be used in the next program run.
     *
     * @param newStorageFolderPath
     *            the absolute path of the directory that data is to be
     *            transferred to
     * @throws InvalidFilePathException
     *             if the program does not have sufficient permissions to carry
     *             out file operations in newStorageFolderPath
     * @throws FilesReplacedException
     *             if existing files had to be replaced (with backups made) in
     *             the newStorageFolderPath
     */
    void changeDirectory(String newStorageFolderPath) throws InvalidFilePathException,
            FilesReplacedException;

    /**
     * This method returns the path of the current storage directory.
     *
     * @return the path of the current storage directory.
     */
    String getCurrentDirectory();

}
