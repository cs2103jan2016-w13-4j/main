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
     * This method resets the program's internal storage of aliases. This method
     * should only be used by the public in tests.
     */
    public static void resetProgramStorage() {
        aliasList = new ArrayList<Alias>();
        deletedAliasList = new ArrayList<Alias>();
    }

    public static void setFilePath(String absoluteFolderPath) {
        filePath = Paths.get(absoluteFolderPath, Constants.FILENAME_ALIAS);
    }

    public static Path getFilePath() {
        return filePath;
    }

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

    public static boolean undestroy(String alias) {
        Alias deletedAlias = null;
        // Start searching from the back to undestroy the latest alias
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

    public boolean createAndPersist() {
        if (isDuplicate(this)) {
            return false;
        }

        aliasList.add(this);
        Alias.persist();
        return true;
    }

    public static void persist() {
        String json = Serializer.serialize(aliasList);
        FileManager.writeToFile(json, filePath);
    }

    /**
     * @return the alias
     */
    public String getAlias() {
        return alias;
    }

    /**
     * @return the command
     */
    public String getCommand() {
        return command;
    }

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
}
