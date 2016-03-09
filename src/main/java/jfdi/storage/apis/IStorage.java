package jfdi.storage.apis;

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
     * This method initializes storage with data from the folder used in the
     * previous program run. If no existing data files are found, Storage is
     * initialized with no data. If an invalid data files are found at the given
     * location, the invalid files will be renamed and kept as a backup while a
     * new file will overwrite the existing invalid file.
     *
     * @throws ExistingFilesFoundException
     *             if existing unrecognized data files are found and replaced
     *             (with backups made) in the given storageFolderPath
     */
    void initialize() throws ExistingFilesFoundException;

    /**
     * This method can only be executed after load has been executed. It saves
     * and transfers existing data in the current storage folder path into the
     * new storage folder path. The old files are then deleted upon a successful
     * transfer. This new storage path is then stored as the storage path that
     * is to be used in the next program run.
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
     * @throws IllegalAccessException
     *             if changeDirectory is called before Storage is initialized
     */
    void changeDirectory(String newStorageFolderPath) throws InvalidPathException,
            ExistingFilesFoundException, IllegalAccessException;

}
