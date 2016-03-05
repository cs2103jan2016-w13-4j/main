package jfdi.test.storage;

import static org.junit.Assert.*;

import java.nio.file.Files;
import java.nio.file.Path;

import jfdi.storage.Constants;
import jfdi.storage.MainStorage;
import jfdi.storage.data.Alias;
import jfdi.storage.data.AliasAttributes;
import jfdi.storage.data.AliasDb;
import jfdi.storage.exceptions.DuplicateAliasException;
import jfdi.storage.exceptions.InvalidAliasParametersException;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

public class AliasAttributesTest {

    private static Path testDirectory = null;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        testDirectory = Files.createTempDirectory(Constants.TEST_DIRECTORY_NAME);
        MainStorage fileStorageInstance = MainStorage.getInstance();
        fileStorageInstance.load(testDirectory.toString());
    }

    @After
    public void tearDown() throws Exception {
        AliasDb.resetProgramStorage();
    }

    @Test
    public void testConstructorsAndGetters() {
        // Test the first constructor
        Alias alias = new Alias(Constants.TEST_ALIAS, Constants.TEST_COMMAND);
        AliasAttributes aliasAttributes = new AliasAttributes(alias);
        assertEquals(alias.getAlias(), aliasAttributes.getAlias());
        assertEquals(alias.getCommand(), aliasAttributes.getCommand());

        // Test the second constructor
        AliasAttributes aliasAttributes2 = new AliasAttributes(Constants.TEST_ALIAS, Constants.TEST_COMMAND);
        assertEquals(Constants.TEST_ALIAS, aliasAttributes2.getAlias());
        assertEquals(Constants.TEST_COMMAND, aliasAttributes2.getCommand());
    }

    @Test(expected = InvalidAliasParametersException.class)
    public void testInvalidAliasSave() throws Exception {
        AliasAttributes aliasAttributes = new AliasAttributes(null, Constants.TEST_COMMAND);
        aliasAttributes.save();
    }

    @Test(expected = InvalidAliasParametersException.class)
    public void testInvalidCommandSave() throws Exception {
        AliasAttributes aliasAttributes = new AliasAttributes(Constants.TEST_ALIAS, null);
        aliasAttributes.save();
    }

    @Test(expected = DuplicateAliasException.class)
    public void testDuplicateSave() throws Exception {
        AliasAttributes aliasAttributes = new AliasAttributes(Constants.TEST_ALIAS, Constants.TEST_COMMAND);
        aliasAttributes.save();

        // Duplicate save
        aliasAttributes.save();
    }

    @Test
    public void testSave() throws Exception {
        AliasAttributes aliasAttributes = new AliasAttributes(Constants.TEST_ALIAS, Constants.TEST_COMMAND);

        // Make sure that the database is empty before save
        assertTrue(AliasDb.getAll().isEmpty());

        // Command under test
        aliasAttributes.save();

        // Check that the alias has been persisted
        assertEquals(AliasDb.getAll().size(), 1);
        assertEquals(AliasDb.getCommandFromAlias(Constants.TEST_ALIAS), Constants.TEST_COMMAND);
    }

    @Test
    public void testToEntity() {
        AliasAttributes aliasAttributes = new AliasAttributes(Constants.TEST_ALIAS, Constants.TEST_COMMAND);
        Alias alias = aliasAttributes.toEntity();
        assertEquals(alias.getAlias(), aliasAttributes.getAlias());
        assertEquals(alias.getCommand(), aliasAttributes.getCommand());
    }

}
