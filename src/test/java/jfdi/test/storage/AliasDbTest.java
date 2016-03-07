package jfdi.test.storage;

import static org.junit.Assert.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import jfdi.storage.Constants;
import jfdi.storage.MainStorage;
import jfdi.storage.data.AliasAttributes;
import jfdi.storage.data.AliasDb;
import jfdi.storage.exceptions.DuplicateAliasException;
import jfdi.storage.exceptions.InvalidAliasException;
import jfdi.storage.serializer.Serializer;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

public class AliasDbTest {

    private static Path testDirectory = null;
    private static String testDirectoryString = null;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        testDirectory = Files.createTempDirectory(Constants.TEST_DIRECTORY_NAME);
        testDirectoryString = testDirectory.toString();
        MainStorage fileStorageInstance = MainStorage.getInstance();
        fileStorageInstance.load(testDirectory.toString());
    }

    @After
    public void tearDown() throws Exception {
        AliasDb.resetProgramStorage();
    }

    @Test
    public void testCreate() throws Exception {
        AliasAttributes aliasAttributes = new AliasAttributes(Constants.TEST_ALIAS, Constants.TEST_COMMAND);
        AliasDb.create(aliasAttributes);
        assertEquals(AliasDb.getCommandFromAlias(aliasAttributes.getAlias()), aliasAttributes.getCommand());
    }

    @Test(expected = DuplicateAliasException.class)
    public void testCreateDuplicateAlias() throws Exception {
        AliasAttributes aliasAttributes = new AliasAttributes(Constants.TEST_ALIAS, Constants.TEST_COMMAND);
        AliasDb.create(aliasAttributes);

        // This duplicate create triggers the exception
        AliasDb.create(aliasAttributes);
    }

    @Test
    public void testGetAll() throws Exception {
        // Create 2 aliases
        assertTrue(AliasDb.getAll().isEmpty());
        AliasAttributes aliasAttributes = new AliasAttributes(Constants.TEST_ALIAS, Constants.TEST_COMMAND);
        AliasAttributes aliasAttributes2 = new AliasAttributes(Constants.TEST_ALIAS_2,
                Constants.TEST_COMMAND_2);
        aliasAttributes.save();
        aliasAttributes2.save();

        // Make sure that the database contains exactly these 2 aliases
        ArrayList<AliasAttributes> aliasAttributesList = new ArrayList<AliasAttributes>(
                AliasDb.getAll());
        assertEquals(aliasAttributesList.size(), 2);
        assertTrue(contains(aliasAttributesList, aliasAttributes));
        assertTrue(contains(aliasAttributesList, aliasAttributes2));
    }

    @Test
    public void testHasAlias() throws Exception {
        // No aliases exist yet, so any alias should be invalid
        assertFalse(AliasDb.hasAlias(Constants.TEST_ALIAS));

        // Create an alias
        AliasAttributes aliasAttributes = new AliasAttributes(Constants.TEST_ALIAS, Constants.TEST_COMMAND);
        aliasAttributes.save();

        // Check that AliasDb has that alias
        assertTrue(AliasDb.hasAlias(Constants.TEST_ALIAS));
    }

    @Test
    public void testDestroyAndUndestroy() throws Exception {
        // Create an alias
        AliasAttributes aliasAttributes = new AliasAttributes(Constants.TEST_ALIAS, Constants.TEST_COMMAND);
        aliasAttributes.save();
        assertEquals(AliasDb.getAll().size(), 1);

        // Destroy it and check that the database is empty
        AliasDb.destroy(aliasAttributes.getAlias());
        assertEquals(AliasDb.getAll().size(), 0);

        // Undestroy it and check that it's back in the database
        AliasDb.undestroy(aliasAttributes.getAlias());
        assertEquals(AliasDb.getAll().size(), 1);
    }

    @Test
    public void testGetCommandFromAlias() throws Exception {
        AliasAttributes aliasAttributes = new AliasAttributes(Constants.TEST_ALIAS, Constants.TEST_COMMAND);
        aliasAttributes.save();
        assertEquals(AliasDb.getCommandFromAlias(aliasAttributes.getAlias()), aliasAttributes.getCommand());
    }

    @Test(expected = InvalidAliasException.class)
    public void testGetCommandFromInvalidAlias() throws Exception {
        AliasDb.getCommandFromAlias(Constants.TEST_ALIAS);
    }

    @Test
    public void testLoad() throws Exception {
        // Create an alias
        AliasAttributes aliasAttributes = new AliasAttributes(Constants.TEST_ALIAS, Constants.TEST_COMMAND);
        aliasAttributes.save();

        // Get the JSON form of the current state
        String json = Serializer.serialize(AliasDb.getAll());

        // Remove the alias
        AliasDb.destroy(aliasAttributes.getAlias());
        assertTrue(AliasDb.getAll().isEmpty());

        // Create the data file and load from it
        TestHelper.createDataFilesWith(testDirectoryString, json);
        AliasDb.load();

        // Check that the original alias exists
        assertEquals(AliasDb.getAll().size(), 1);
        assertEquals(AliasDb.getCommandFromAlias(aliasAttributes.getAlias()), aliasAttributes.getCommand());
    }

    @Test
    public void testSetAndGetFilePath() {
        Path subdirectory = Paths.get(testDirectory.toString(), Constants.TEST_SUBDIRECTORY_NAME);
        Path expectedAliasPath = Paths.get(subdirectory.toString(), Constants.FILENAME_ALIAS);
        AliasDb.setFilePath(subdirectory.toString());
        assertEquals(expectedAliasPath, AliasDb.getFilePath());

        // Reset back to the original file path
        AliasDb.setFilePath(testDirectoryString);
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
