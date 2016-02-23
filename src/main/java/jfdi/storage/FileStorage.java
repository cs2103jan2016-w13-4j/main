package jfdi.storage;

import java.nio.file.InvalidPathException;

import jfdi.storage.exceptions.ExistingFilesFoundException;

/**
 * This class serves as the facade of the Storage component.
 *
 * @author Thng Kai Yuan
 */
public class FileStorage implements IStorage {

    // The singleton instance of FileStorage
    private static FileStorage instance = null;

    // Boolean indicating if storage has been initialized
    private boolean isInitialized = false;

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
        isInitialized = true;
    }

    @Override
    public void changeDirectory(String newStorageFolderPath) throws InvalidPathException,
            ExistingFilesFoundException, IllegalAccessException {

        if (!isInitialized) {
            throw new IllegalAccessException(Constants.MESSAGE_UNINITIALIZED_STORAGE);
        }

        FileManager.prepareDirectory(newStorageFolderPath);
        FileManager.moveFilesToDirectory(newStorageFolderPath);
        RecordManager.setAllFilePaths(newStorageFolderPath);
        RecordManager.loadAllRecords();
    }

    /**
     * This method sets the existing instance to null. It should only be used
     * for testing/debugging purposes only.
     */
    public void removeInstance() {
        instance = null;
    }

}
