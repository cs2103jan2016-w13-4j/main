//@@author A0121621Y
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
        aliasDbInstance.reset();
    }

    @Test
    public void testCreate() throws Exception {
        // Create an Alias from an AliasAttributes
        AliasAttributes aliasAttributes = new AliasAttributes(Constants.TEST_ALIAS, Constants.TEST_COMMAND);
        aliasDbInstance.create(aliasAttributes);

        // Assert that the properties remain the same
        assertEquals(aliasAttributes.getCommand(), aliasDbInstance.getCommandFromAlias(aliasAttributes.getAlias()));
    }

    @Test(expected = DuplicateAliasException.class)
    public void testCreateDuplicateAlias() throws Exception {
        // Create the first instance of the Alias
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
        assertEquals(2, aliasAttributesList.size());
        assertTrue(contains(aliasAttributesList, aliasAttributes));
        assertTrue(contains(aliasAttributesList, aliasAttributes2));
    }

    @Test
    public void testHasAlias() throws Exception {
        // No aliases exist yet, so checking any alias should return false
        assertFalse(aliasDbInstance.hasAlias(Constants.TEST_ALIAS));

        // Create an alias
        AliasAttributes aliasAttributes = new AliasAttributes(Constants.TEST_ALIAS, Constants.TEST_COMMAND);
        aliasAttributes.save();

        // Check that aliasDbInstance has that alias
        assertTrue(aliasDbInstance.hasAlias(Constants.TEST_ALIAS));
    }

    @Test
    public void testDestroyAndUndestroy() throws Exception {
        // Create an alias
        AliasAttributes aliasAttributes = new AliasAttributes(Constants.TEST_ALIAS, Constants.TEST_COMMAND);
        aliasAttributes.save();
        assertEquals(1, aliasDbInstance.getAll().size());

        // Destroy it and check that the database is empty
        aliasDbInstance.destroy(aliasAttributes.getAlias());
        assertEquals(0, aliasDbInstance.getAll().size());

        // Undestroy it and check that it's back in the database
        aliasDbInstance.undestroy(aliasAttributes.getAlias());
        assertEquals(1, aliasDbInstance.getAll().size());
    }

    @Test
    public void testGetCommandFromAlias() throws Exception {
        // Create an Alias in the database
        AliasAttributes aliasAttributes = new AliasAttributes(Constants.TEST_ALIAS, Constants.TEST_COMMAND);
        aliasAttributes.save();

        // Assert the the command we obtain is the same as the one we set
        assertEquals(aliasAttributes.getCommand(), aliasDbInstance.getCommandFromAlias(aliasAttributes.getAlias()));
    }

    @Test(expected = InvalidAliasException.class)
    public void testGetCommandFromInvalidAlias() throws Exception {
        // Try getting the command of an inexistent alias
        aliasDbInstance.getCommandFromAlias(Constants.TEST_ALIAS);
    }

    @Test
    public void testLoad() throws Exception {
        // Create an alias and get the JSON form of the current state
        AliasAttributes aliasAttributes = new AliasAttributes(Constants.TEST_ALIAS, Constants.TEST_COMMAND);
        aliasAttributes.save();
        String json = Serializer.serialize(aliasDbInstance.getAll());

        // Remove the alias and create the data file
        aliasDbInstance.destroy(aliasAttributes.getAlias());
        assertTrue(aliasDbInstance.getAll().isEmpty());
        TestHelper.createDataFilesWith(testDirectoryString, json);

        // Command under test (load from data file)
        FilePathPair replacedFiles = aliasDbInstance.load();

        // Ensure that no files were replaced
        assertNull(replacedFiles);

        // Check that the original alias exists
        assertEquals(aliasDbInstance.getAll().size(), 1);
        assertEquals(aliasDbInstance.getCommandFromAlias(aliasAttributes.getAlias()), aliasAttributes.getCommand());
    }

    @Test
    public void testInvalidLoad() throws Exception {
        // Create an invalid alias data file to load from
        TestHelper.createInvalidAliasFile(testDirectoryString);

        // Command under test (load from the invalid data file)
        FilePathPair replacedFiles = aliasDbInstance.load();

        // Assert that the invalid file was moved
        assertNotNull(replacedFiles);
    }

    @Test
    public void testSetAndGetFilePath() {
        // Get the original file path so that we can revert it later
        Path originalFilePath = aliasDbInstance.getFilePath();

        // Set up the paths that we want to use during the test
        Path subdirectory = Paths.get(testDirectory.toString(), Constants.TEST_SUBDIRECTORY_NAME);
        Path expectedAliasPath = Paths.get(subdirectory.toString(), Constants.FILENAME_ALIAS);

        // Command under test
        aliasDbInstance.setFilePath(subdirectory.toString());

        // Assert that the file path is now set correctly
        assertEquals(expectedAliasPath, aliasDbInstance.getFilePath());

        // Reset back to the original file path
        aliasDbInstance.setFilePath(originalFilePath.getParent().toString());
    }

    /**
     * This method checks if aliasAttributesList contains an AliasAttributes
     * that is equal to aliasAttributes.
     *
     * @param aliasAttributesList
     *            the list that we want to check
     * @param aliasAttributes
     *            the aliasAttributes that we want to find in
     *            aliasAttributesList
     * @return a boolean indicating if aliasAttributesList contains an
     *         AliasAttributes that is equal to aliasAttributes
     */
    private boolean contains(ArrayList<AliasAttributes> aliasAttributesList, AliasAttributes aliasAttributes) {
        return aliasAttributesList.stream().anyMatch(aliasAttributes2 -> {
            return aliasAttributes2.getAlias().equals(aliasAttributes.getAlias())
                    && aliasAttributes2.getCommand().equals(aliasAttributes.getCommand());
        });
    }

}
