package jfdi.storage.records;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import jfdi.storage.Constants;
import jfdi.storage.FileManager;
import jfdi.storage.exceptions.FilePathPair;
import jfdi.storage.serializer.Serializer;

public class Alias {

    private static ArrayList<Alias> aliasList = new ArrayList<Alias>();
    private static ArrayList<Alias> deletedAliasList = new ArrayList<Alias>();

    // The filepath to the data file
    private static Path filePath = null;

    private String alias;
    private String command;

    public Alias(String alias, String command) {
        this.alias = alias;
        this.command = command;
    }

    public static void setFilePath(String absoluteFolderPath) {
        filePath = Paths.get(absoluteFolderPath, Constants.FILENAME_ALIAS);
    }

    public static Path getFilePath() {
        return filePath;
    }

    /**
     * This method returns all the aliases currently stored in the program's
     * internal state. The returned collection may not be synchronized with the
     * disk data if the method is called before data is persisted to disk.
     *
     * @return a Collection of aliases currently stored in the program
     */
    public static Collection<Alias> getAllAliases() {
        return aliasList;
    }

    /**
     * This method resets the program's internal storage of aliases. This method
     * should only be used by the public in tests.
     */
    public static void resetProgramStorage() {
        aliasList = new ArrayList<Alias>();
        deletedAliasList = new ArrayList<Alias>();
    }

    /**
     * This method loads the aliases currently stored in the file given by filePath
     *
     * @return a FilePathPair if the existing file cannot be read and was renamed
     */
    public static FilePathPair load() {
        File dataFile = filePath.toFile();
        if (!dataFile.exists()) {
            return null;
        }

        String persistedJsonData = FileManager.readFileToString(filePath);
        Alias[] aliasArray = Serializer.deserialize(persistedJsonData, Alias[].class);
        if (aliasArray != null) {
            aliasList = new ArrayList<Alias>(Arrays.asList(aliasArray));
            deletedAliasList = new ArrayList<Alias>();
            return null;
        } else {
            resetProgramStorage();
        }

        String movedTo = FileManager.backupAndRemove(filePath);
        return new FilePathPair(filePath.toString(), movedTo);
    }

    /**
     * This method returns the command corresponding to an alias currently
     * stored in the program
     *
     * @param alias
     *            the alias whose command we want to retrieve
     * @return the command if the given alias exists in the program's storage,
     *         null otherwise
     */
    public static String getCommandFromAlias(String alias) {
        String alias2 = null;
        for (Alias aliasRecord : aliasList) {
            alias2 = aliasRecord.getAlias();
            if (alias.equals(alias2)) {
                return aliasRecord.getCommand();
            }
        }

        return null;
    }

    /**
     * This method soft-deletes an existing alias.
     *
     * @param alias
     *          the alias that we want to delete
     * @return boolean indicating if the alias was deleted
     */
    public static boolean destroy(String alias) {
        String alias2 = null;
        for (Alias aliasRecord : aliasList) {
            alias2 = aliasRecord.getAlias();
            if (alias.equals(alias2)) {
                aliasList.remove(aliasRecord);
                deletedAliasList.add(aliasRecord);
                return true;
            }
        }

        return false;
    }

    /**
     * This method restores an alias that was soft-deleted earlier.
     *
     * @param alias
     *          the alias that we want to recover
     * @return boolean indicating if the alias was recovered
     */
    public static boolean undestroy(String alias) {
        Alias deletedAlias = null;

        // Start searching from the back to undestroy the latest matching alias
        for (int i = deletedAliasList.size() - 1; i >= 0; i--) {
            deletedAlias = deletedAliasList.get(i);
            if (alias.equals(deletedAlias.getAlias()) && !isDuplicate(deletedAlias)) {
                deletedAliasList.remove(i);
                aliasList.add(deletedAlias);
                return true;
            }
        }

        return false;
    }

    /**
     * This method checks if the given alias already exists in the program's
     * storage.
     *
     * @param alias
     *            the alias that we want to check
     * @return boolean indicating if the alias already exists
     */
    private static boolean isDuplicate(Alias alias) {
        String alias1 = alias.getAlias();
        String alias2;
        for (Alias aliasRecord : aliasList) {
            alias2 = aliasRecord.getAlias();
            if (alias1.equals(alias2)) {
                return true;
            }
        }

        return false;
    }

    /**
     * This method creates the new alias record and persists all existing Alias
     * records in the program to the file system.
     *
     * @return boolean indicating if the alias was created and persisted
     */
    public boolean createAndPersist() {
        if (isDuplicate(this)) {
            return false;
        }

        aliasList.add(this);
        Alias.persist();
        return true;
    }

    /**
     * This method persists all existing aliases to the file system.
     */
    public static void persist() {
        String json = Serializer.serialize(aliasList);
        FileManager.writeToFile(json, filePath);
    }

    public String getAlias() {
        return alias;
    }

    public String getCommand() {
        return command;
    }
}
