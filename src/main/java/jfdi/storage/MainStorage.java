package jfdi.storage;

import java.nio.file.InvalidPathException;

import jfdi.storage.exceptions.ExistingFilesFoundException;

/**
 * This class serves as the facade of the Storage component.
 *
 * @author Thng Kai Yuan
 */
public class MainStorage implements IStorage {

    // The singleton instance of MainStorage
    private static MainStorage instance = null;

    // Boolean indicating if storage has been initialized
    private boolean isInitialized = false;

    /**
     * This private constructor prevents itself from being called by other
     * components. An instance of FileStorage should be initialized using the
     * getInstance method.
     */
    private MainStorage() {
    }

    /**
     * @return the singleton instance of MainStorage
     */
    public static MainStorage getInstance() {
        if (instance == null) {
            instance = new MainStorage();
        }

        return instance;
    }

    /**
     * This method sets the existing instance to null. It should only be used
     * for testing/debugging purposes only.
     */
    public void removeInstance() {
        instance = null;
    }

    @Override
    public void load(String storageFolderPath) throws InvalidPathException, ExistingFilesFoundException {
        FileManager.prepareDirectory(storageFolderPath);
        DatabaseManager.setAllFilePaths(storageFolderPath);
        DatabaseManager.loadAllDatabases();
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
        DatabaseManager.setAllFilePaths(newStorageFolderPath);
        DatabaseManager.loadAllDatabases();
    }

}
