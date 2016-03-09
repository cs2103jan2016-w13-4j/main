package jfdi.storage.apis;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import jfdi.storage.Constants;
import jfdi.storage.FileManager;
import jfdi.storage.IDatabase;
import jfdi.storage.entities.Alias;
import jfdi.storage.exceptions.DuplicateAliasException;
import jfdi.storage.exceptions.FilePathPair;
import jfdi.storage.exceptions.InvalidAliasException;
import jfdi.storage.serializer.Serializer;

public class AliasDb implements IDatabase {

    // Singleton instance of AliasDb
    private static AliasDb instance = null;

    // The list of existing and deleted aliases
    private ArrayList<Alias> aliasList = new ArrayList<Alias>();
    private ArrayList<Alias> deletedAliasList = new ArrayList<Alias>();

    // The filepath to the data file
    private Path filePath = null;

    /**
     * This private constructor prevents more instances of AliasDb from being
     * created.
     */
    private AliasDb() {}

    /**
     * This method returns the singleton instance of AliasDb.
     *
     * @return the singleton instance of AliasDb
     */
    public static AliasDb getInstance() {
        if (instance == null) {
            instance = new AliasDb();
        }
        return instance;
    }

    /**
     * This method creates a new alias if the given aliasAttributes is not a
     * duplicate of an existing alias in the database.
     *
     * @param aliasAttributes
     *            the mapping of alias to command that is to be created
     * @throws DuplicateAliasException
     *             if the given alias already exists in the database
     */
    public void create(AliasAttributes aliasAttributes) throws DuplicateAliasException {
        if (isDuplicate(aliasAttributes)) {
            throw new DuplicateAliasException(aliasAttributes);
        }
        aliasList.add(aliasAttributes.toEntity());
        persist();
    }

    /**
     * This method returns all the aliases (in the form of their attributes)
     * currently stored in the program's internal state.
     *
     * @return a Collection of AliasAttributes
     */
    public Collection<AliasAttributes> getAll() {
        ArrayList<AliasAttributes> aliasAttributes = new ArrayList<AliasAttributes>();
        for (Alias alias : aliasList) {
            aliasAttributes.add(new AliasAttributes(alias));
        }
        return aliasAttributes;
    }

    /**
     * This method returns the command corresponding to an alias currently
     * stored in the program
     *
     * @param alias
     *            the alias whose command we want to retrieve
     * @return the command if the given alias exists in the program's storage
     * @throws InvalidAliasException
     *             if the alias does not exist in the database
     */
    public String getCommandFromAlias(String alias) throws InvalidAliasException {
        String alias2 = null;
        for (Alias aliasRecord : aliasList) {
            alias2 = aliasRecord.getAlias();
            if (alias.equals(alias2)) {
                return aliasRecord.getCommand();
            }
        }

        throw new InvalidAliasException(alias);
    }

    /**
     * This method checks if a given alias exists in the database.
     *
     * @param alias
     *            the alias to be checked
     * @return boolean indicating if the alias exists in the database
     */
    public boolean hasAlias(String alias) {
        assert alias != null;
        String alias2 = null;
        for (Alias aliasRecord : aliasList) {
            alias2 = aliasRecord.getAlias();
            if (alias.equals(alias2)) {
                return true;
            }
        }
        return false;
    }

    /**
     * This method soft-deletes an existing alias.
     *
     * @param alias
     *            the alias that we want to delete
     * @throws InvalidAliasException
     *             if the alias does not exist in the database
     */
    public void destroy(String alias) throws InvalidAliasException {
        String alias2 = null;
        for (Alias aliasRecord : aliasList) {
            alias2 = aliasRecord.getAlias();
            if (alias.equals(alias2)) {
                aliasList.remove(aliasRecord);
                deletedAliasList.add(aliasRecord);
                return;
            }
        }

        throw new InvalidAliasException(alias);
    }

    /**
     * This method restores an alias that was soft-deleted earlier.
     *
     * @param alias
     *            the alias that we want to recover
     * @throws InvalidAliasException
     *             if the alias does not exist in the database
     */
    public void undestroy(String alias) throws InvalidAliasException {
        Alias deletedAlias = null;

        // Start searching from the back to undestroy the latest matching alias
        for (int i = deletedAliasList.size() - 1; i >= 0; i--) {
            deletedAlias = deletedAliasList.get(i);
            if (alias.equals(deletedAlias.getAlias()) && !isDuplicate(new AliasAttributes(deletedAlias))) {
                deletedAliasList.remove(i);
                aliasList.add(deletedAlias);
                return;
            }
        }

        throw new InvalidAliasException(alias);
    }

    /**
     * This method checks if the given alias already exists in the program's
     * storage.
     *
     * @param alias
     *            the alias that we want to check
     * @return boolean indicating if the alias already exists
     */
    private boolean isDuplicate(AliasAttributes aliasAttributes) {
        String alias1 = aliasAttributes.getAlias();
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
     * This method persists all existing aliases to the file system.
     */
    public void persist() {
        String json = Serializer.serialize(aliasList);
        FileManager.writeToFile(json, filePath);
    }

    /**
     * This method resets the program's internal storage of aliases. This method
     * should only be used by the public in tests.
     */
    public void resetProgramStorage() {
        aliasList = new ArrayList<Alias>();
        deletedAliasList = new ArrayList<Alias>();
    }

    /**
     * This method sets the filepath of the record using absoluteFolderPath to
     * store the record's data file.
     *
     * @param absoluteFolderPath
     *            the directory that should contain the data file
     */
    public void setFilePath(String absoluteFolderPath) {
        filePath = Paths.get(absoluteFolderPath, Constants.FILENAME_ALIAS);
    }

    /**
     * @return the path of the record as set using the setFilePath method
     */
    public Path getFilePath() {
        return filePath;
    }

    /**
     * This method loads the aliases currently stored in the file given by
     * filePath
     *
     * @return a FilePathPair if the existing file cannot be read and was
     *         renamed
     */
    public FilePathPair load() {
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

}
