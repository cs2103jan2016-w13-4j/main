package jfdi.test.storage;

import static org.junit.Assert.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import jfdi.storage.Constants;
import jfdi.storage.apis.AliasAttributes;
import jfdi.storage.apis.AliasDb;
import jfdi.storage.apis.MainStorage;
import jfdi.storage.exceptions.DuplicateAliasException;
import jfdi.storage.exceptions.FilePathPair;
import jfdi.storage.exceptions.InvalidAliasException;
import jfdi.storage.serializer.Serializer;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class AliasDbTest {

    private static Path testDirectory = null;
    private static String testDirectoryString = null;
    private static AliasDb aliasDbInstance = null;
    private static MainStorage mainStorageInstance = null;
    private static String originalPreference = null;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        testDirectory = Files.createTempDirectory(Constants.TEST_DIRECTORY_NAME);
        testDirectoryString = testDirectory.toString();
        aliasDbInstance = AliasDb.getInstance();
        AliasAttributes.setCommandRegex(Constants.TEST_COMMAND_REGEX);
        mainStorageInstance = MainStorage.getInstance();
        originalPreference = mainStorageInstance.getPreferredDirectory();
        mainStorageInstance.setPreferredDirectory(testDirectoryString);
        mainStorageInstance.initialize();
        mainStorageInstance.use(testDirectoryString);
    }

    @AfterClass
    public static void tearDownAfterClass() {
        TestHelper.revertOriginalPreference(mainStorageInstance, originalPreference);
    }

    @After
    public void tearDown() throws Exception {
        aliasDbInstance.resetProgramStorage();
    }

    @Test
    public void testCreate() throws Exception {
        AliasAttributes aliasAttributes = new AliasAttributes(Constants.TEST_ALIAS, Constants.TEST_COMMAND);
        aliasDbInstance.create(aliasAttributes);
        assertEquals(aliasDbInstance.getCommandFromAlias(aliasAttributes.getAlias()), aliasAttributes.getCommand());
    }

    @Test(expected = DuplicateAliasException.class)
    public void testCreateDuplicateAlias() throws Exception {
        AliasAttributes aliasAttributes = new AliasAttributes(Constants.TEST_ALIAS, Constants.TEST_COMMAND);
        aliasDbInstance.create(aliasAttributes);

        // This duplicate create triggers the exception
        aliasDbInstance.create(aliasAttributes);
    }

    @Test
    public void testGetAll() throws Exception {
        // Create 2 aliases
        assertTrue(aliasDbInstance.getAll().isEmpty());
        AliasAttributes aliasAttributes = new AliasAttributes(Constants.TEST_ALIAS, Constants.TEST_COMMAND);
        AliasAttributes aliasAttributes2 = new AliasAttributes(Constants.TEST_ALIAS_2,
                Constants.TEST_COMMAND_2);
        aliasAttributes.save();
        aliasAttributes2.save();

        // Make sure that the database contains exactly these 2 aliases
        ArrayList<AliasAttributes> aliasAttributesList = new ArrayList<AliasAttributes>(
                aliasDbInstance.getAll());
        assertEquals(aliasAttributesList.size(), 2);
        assertTrue(contains(aliasAttributesList, aliasAttributes));
        assertTrue(contains(aliasAttributesList, aliasAttributes2));
    }

    @Test
    public void testHasAlias() throws Exception {
        // No aliases exist yet, so any alias should be invalid
        assertFalse(aliasDbInstance.hasAlias(Constants.TEST_ALIAS));

        // Create an alias
        AliasAttributes aliasAttributes = new AliasAttributes(Constants.TEST_ALIAS, Constants.TEST_COMMAND);
        aliasAttributes.save();

        // Check that aliasDbInstance.has that alias
        assertTrue(aliasDbInstance.hasAlias(Constants.TEST_ALIAS));
    }

    @Test
    public void testDestroyAndUndestroy() throws Exception {
        // Create an alias
        AliasAttributes aliasAttributes = new AliasAttributes(Constants.TEST_ALIAS, Constants.TEST_COMMAND);
        aliasAttributes.save();
        assertEquals(aliasDbInstance.getAll().size(), 1);

        // Destroy it and check that the database is empty
        aliasDbInstance.destroy(aliasAttributes.getAlias());
        assertEquals(aliasDbInstance.getAll().size(), 0);

        // Undestroy it and check that it's back in the database
        aliasDbInstance.undestroy(aliasAttributes.getAlias());
        assertEquals(aliasDbInstance.getAll().size(), 1);
    }

    @Test
    public void testGetCommandFromAlias() throws Exception {
        AliasAttributes aliasAttributes = new AliasAttributes(Constants.TEST_ALIAS, Constants.TEST_COMMAND);
        aliasAttributes.save();
        assertEquals(aliasDbInstance.getCommandFromAlias(aliasAttributes.getAlias()), aliasAttributes.getCommand());
    }

    @Test(expected = InvalidAliasException.class)
    public void testGetCommandFromInvalidAlias() throws Exception {
        aliasDbInstance.getCommandFromAlias(Constants.TEST_ALIAS);
    }

    @Test
    public void testLoad() throws Exception {
        // Create an alias
        AliasAttributes aliasAttributes = new AliasAttributes(Constants.TEST_ALIAS, Constants.TEST_COMMAND);
        aliasAttributes.save();

        // Get the JSON form of the current state
        String json = Serializer.serialize(aliasDbInstance.getAll());

        // Remove the alias
        aliasDbInstance.destroy(aliasAttributes.getAlias());
        assertTrue(aliasDbInstance.getAll().isEmpty());

        // Create the data file and load from it
        TestHelper.createDataFilesWith(testDirectoryString, json);
        FilePathPair replacedFiles = aliasDbInstance.load();

        // Ensure that no files were replaced
        assertNull(replacedFiles);

        // Check that the original alias exists
        assertEquals(aliasDbInstance.getAll().size(), 1);
        assertEquals(aliasDbInstance.getCommandFromAlias(aliasAttributes.getAlias()), aliasAttributes.getCommand());
    }

    @Test
    public void testInvalidLoad() throws Exception {
        TestHelper.createInvalidAliasFile(testDirectoryString);
        FilePathPair replacedFiles = aliasDbInstance.load();
        assertNotNull(replacedFiles);
    }

    @Test
    public void testSetAndGetFilePath() {
        Path originalFilePath = aliasDbInstance.getFilePath();
        Path subdirectory = Paths.get(testDirectory.toString(), Constants.TEST_SUBDIRECTORY_NAME);
        Path expectedAliasPath = Paths.get(subdirectory.toString(), Constants.FILENAME_ALIAS);
        aliasDbInstance.setFilePath(subdirectory.toString());
        assertEquals(expectedAliasPath, aliasDbInstance.getFilePath());

        // Reset back to the original file path
        aliasDbInstance.setFilePath(originalFilePath.getParent().toString());
    }

    private boolean contains(ArrayList<AliasAttributes> aliasAttributesList, AliasAttributes aliasAttributes) {
        for (AliasAttributes aliasAttributes2 : aliasAttributesList) {
            if (aliasAttributes2.getAlias().equals(aliasAttributes.getAlias())
                    && aliasAttributes2.getCommand().equals(aliasAttributes.getCommand())) {
                return true;
            }
        }
        return false;
    }

}
