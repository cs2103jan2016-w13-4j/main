package jfdi.storage;

import jfdi.storage.exceptions.ExistingFilesFoundException;
import jfdi.storage.exceptions.InvalidPathException;

/**
 * This class serves as the facade of the Storage component.
 *
 * @author Thng Kai Yuan
 */
public class FileStorage implements Storage {

    // The singleton instance of FileStorage
    private static FileStorage instance = null;

    /**
     * This private constructor prevents itself from being called by other
     * components. An instance of FileStorage should be initialized using the
     * getInstance method.
     */
    private FileStorage() {
    }

    /**
     * @return the singleton instance of FileStorage
     */
    public static FileStorage getInstance() {
        if (instance == null) {
            instance = new FileStorage();
        }

        return instance;
    }

    @Override
    public void load(String storageFolderPath) throws InvalidPathException, ExistingFilesFoundException {
        FileManager.prepareDirectory(storageFolderPath);
        RecordManager.setAllFilePaths(storageFolderPath);
        RecordManager.loadAllRecords();
    }

    @Override
    public void changeDirectory(String newStorageFolderPath) throws InvalidPathException,
    ExistingFilesFoundException {
        FileManager.prepareDirectory(newStorageFolderPath);
        FileManager.moveFilesToDirectory(newStorageFolderPath);
        RecordManager.setAllFilePaths(newStorageFolderPath);
        RecordManager.loadAllRecords();
    }

}
