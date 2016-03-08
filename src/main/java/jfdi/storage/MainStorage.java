package jfdi.storage;

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

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
    public void initialize() throws ExistingFilesFoundException {
        load(getInitializationPath());
        isInitialized = true;
    }

    @Override
    public void changeDirectory(String newStorageFolderPath) throws InvalidPathException,
            ExistingFilesFoundException, IllegalAccessException {

        if (!isInitialized) {
            throw new IllegalAccessException(Constants.MESSAGE_UNINITIALIZED_STORAGE);
        }

        DatabaseManager.persistAll();
        FileManager.prepareDirectory(newStorageFolderPath);
        FileManager.moveFilesToDirectory(newStorageFolderPath);
        DatabaseManager.setAllFilePaths(newStorageFolderPath);
        setPreferredDirectory(newStorageFolderPath);
    }

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
    public void load(String storageFolderPath) throws InvalidPathException, ExistingFilesFoundException {
        FileManager.prepareDirectory(storageFolderPath);
        DatabaseManager.setAllFilePaths(storageFolderPath);
        DatabaseManager.loadAllDatabases();
    }

    /**
     * This method returns the storage path that should be used for the initial
     * load. If a preferred directory is found, it is used. Otherwise, we use
     * the default directory.
     *
     * @return the directory that should be used for the initial load
     */
    private String getInitializationPath() {
        String preferredDirectory = getPreferredDirectory();
        if (preferredDirectory != null) {
            return preferredDirectory;
        }
        return getDefaultDirectory();
    }

    /**
     * This method returns the default directory that stores the path of the
     * preferred storage directory.
     *
     * @return the default directory
     */
    private String getDefaultDirectory() {
        return Constants.PATH_DEFAULT_DIRECTORY;
    }

    /**
     * This method returns the preferred directory stored in the preference file
     * if the stored path is valid.
     *
     * @return the stored preference if it is valid
     */
    public String getPreferredDirectory() {
        if (!Files.exists(Constants.PATH_PREFERENCE_FILE)) {
            return null;
        }

        String preference = FileManager.readFileToString(Constants.PATH_PREFERENCE_FILE);
        try {
            Path preferredDirectory = Paths.get(preference);
            return preferredDirectory.toString();
        } catch (InvalidPathException e) {
            return null;
        }
    }

    /**
     * This method creates a preference file with the given preferredDirectory
     * as the content of this file.
     *
     * @param preferredDirectory
     *            the preferred storage directory
     */
    public void setPreferredDirectory(String preferredDirectory) {
        FileManager.writeToFile(preferredDirectory, Constants.PATH_PREFERENCE_FILE);
    }

}
