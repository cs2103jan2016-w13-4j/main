package jfdi.storage;

import java.nio.file.InvalidPathException;

import jfdi.storage.exceptions.ExistingFilesFoundException;

/**
 * The Storage interface serves as the facade of the Storage component. It
 * allows the storage folder path to be initialized/changed.
 *
 * @author Thng Kai Yuan
 */
public interface IStorage {

    /**
     * This method loads any existing data from the given storageFolderPath. If
     * no existing data is found, Storage is initialized with no data. If an
     * invalid file is found at the given location, the invalid file will be
     * renamed and kept as a backup while a new file will overwrite the existing
     * invalid file.
     *
     * @param storageFolderPath
     *            the absolute path of the directory that data is to be loaded
     *            from and saved to
     * @throws InvalidPathException
     *             if the program does not have sufficient permissions to carry
     *             out file operations in storageFolderPath
     * @throws ExistingFilesFoundException
     *             if existing unrecognized data files are found and replaced
     *             (with backups made) in the given storageFolderPath
     */
    void load(String storageFolderPath) throws InvalidPathException, ExistingFilesFoundException;

    /**
     * This method can only be executed after load has been executed. It
     * transfers existing data in the current storage folder path into the new
     * storage folder path. The old files are then deleted upon a successful
     * transfer.
     *
     * @param newStorageFolderPath
     *            the absolute path of the directory that data is to be
     *            transferred to
     * @throws InvalidPathException
     *             if the program does not have sufficient permissions to carry
     *             out file operations in newStorageFolderPath
     * @throws ExistingFilesFoundException
     *             if existing files had to be replaced (with backups made) in
     *             the newStorageFolderPath
     */
    void changeDirectory(String newStorageFolderPath) throws InvalidPathException, ExistingFilesFoundException;

}
