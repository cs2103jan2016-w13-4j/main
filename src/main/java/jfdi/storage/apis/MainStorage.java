package jfdi.storage.apis;

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

import jfdi.common.utilities.JfdiLogger;
import jfdi.storage.Constants;
import jfdi.storage.DatabaseManager;
import jfdi.storage.FileManager;
import jfdi.storage.exceptions.FilesReplacedException;

/**
 * This class deals with all the file path operations within the Storage
 * component.
 *
 * @author Thng Kai Yuan
 */
public class MainStorage implements IStorage {

    // The singleton instance of MainStorage
    private static MainStorage instance = null;

    // Boolean indicating if storage has been initialized
    private boolean isInitialized = false;

    // Path to the current storage directory
    private String currentDirectory = null;

    // Logger for events
    private Logger logger = null;

    /**
     * This private constructor prevents itself from being called by other
     * components. An instance of FileStorage should be initialized using the
     * getInstance method.
     */
    private MainStorage() {
        logger = JfdiLogger.getLogger();
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
    public void initialize() throws FilesReplacedException {
        String storageDirectory = getInitializationPath();
        String dataDirectory = getDataDirectory(storageDirectory);
        load(dataDirectory);
        isInitialized = true;
        setCurrentDirectory(storageDirectory);
    }

    @Override
    public void use(String newStorageFolderPath) throws InvalidPathException, FilesReplacedException {
        assert isInitialized;
        load(getDataDirectory(newStorageFolderPath));
        setPreferredDirectory(newStorageFolderPath);
        setCurrentDirectory(newStorageFolderPath);
    }

    @Override
    public void changeDirectory(String newStorageFolderPath) throws InvalidPathException, FilesReplacedException {
        assert isInitialized;
        String newDataDirectory = getDataDirectory(newStorageFolderPath);
        FileManager.prepareDirectory(newDataDirectory);
        FileManager.moveFilesToDirectory(newDataDirectory);
        DatabaseManager.setAllFilePaths(newDataDirectory);
        setPreferredDirectory(newStorageFolderPath);
        setCurrentDirectory(newStorageFolderPath);
    }

    @Override
    public String getCurrentDirectory() {
        assert isInitialized;
        return currentDirectory;
    }

    /**
     * @param currentDirectory the currentDirectory to set
     */
    private void setCurrentDirectory(String currentDirectory) {
        assert currentDirectory != null;
        Path absolutePath = Paths.get(currentDirectory).toAbsolutePath();
        this.currentDirectory = absolutePath.toString();
        logger.fine(String.format(Constants.MESSAGE_LOG_SET_DIRECTORY, absolutePath));
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
     * @throws FilesReplacedException
     *             if existing unrecognized data files are found and replaced
     *             (with backups made) in the given storageFolderPath
     */
    public void load(String storageFolderPath) throws InvalidPathException, FilesReplacedException {
        assert storageFolderPath != null;
        FileManager.prepareDirectory(storageFolderPath);
        DatabaseManager.setAllFilePaths(storageFolderPath);
        DatabaseManager.loadAllDatabases();
    }

    /**
     * This method returns the path to the data directory within the storage
     * directory.
     *
     * @param storageDirectory
     *            the folder which should store the user data
     * @return the path to the data directory within the storage directory
     */
    public String getDataDirectory(String storageDirectory) {
        assert storageDirectory != null;
        return Paths.get(storageDirectory, Constants.FILENAME_DATA_DIRECTORY).toString();
    }

    /**
     * This method returns the storage path that should be used for the initial
     * load. If a preferred directory is found, it is used. Otherwise, we use
     * the default directory.
     *
     * @return the directory that should be used for the initial load
     */
    private String getInitializationPath() {
        String initDirectory = getPreferredDirectory();
        if (initDirectory == null) {
            initDirectory = getDefaultDirectory();
        }
        return initDirectory;
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

        try {
            String preference = FileManager.readFileToString(Constants.PATH_PREFERENCE_FILE);
            return Paths.get(preference).toString();
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
