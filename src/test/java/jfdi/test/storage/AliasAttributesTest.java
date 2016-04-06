//@@author A0121621Y

package jfdi.test.storage;

import static org.junit.Assert.*;

import java.nio.file.Files;
import java.nio.file.Path;

import jfdi.storage.Constants;
import jfdi.storage.apis.AliasAttributes;
import jfdi.storage.apis.AliasDb;
import jfdi.storage.apis.MainStorage;
import jfdi.storage.entities.Alias;
import jfdi.storage.exceptions.DuplicateAliasException;
import jfdi.storage.exceptions.InvalidAliasParametersException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class AliasAttributesTest {

    private static Path testDirectory = null;
    private static AliasDb aliasDbInstance = null;
    private static String originalCommandRegex = null;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        testDirectory = Files.createTempDirectory(Constants.TEST_DIRECTORY_NAME);
        aliasDbInstance = AliasDb.getInstance();
        originalCommandRegex = AliasAttributes.getCommandRegex();
        AliasAttributes.setCommandRegex(Constants.TEST_COMMAND_REGEX);
        MainStorage.getInstance().load(testDirectory.toString());
    }

    @AfterClass
    public static void tearDownAfterClass() {
        AliasAttributes.setCommandRegex(originalCommandRegex);
    }

    @After
    public void tearDown() throws Exception {
        aliasDbInstance.reset();
    }

    @Test
    public void testConstructorsAndGetters() {
        // Test the first constructor (i.e. AliasAttributes(Alias))
        Alias alias = new Alias(Constants.TEST_ALIAS, Constants.TEST_COMMAND);
        AliasAttributes aliasAttributes = new AliasAttributes(alias);
        assertEquals(alias.getAlias(), aliasAttributes.getAlias());
        assertEquals(alias.getCommand(), aliasAttributes.getCommand());

        // Test the second constructor (i.e. AliasAttributes(alias, command))
        AliasAttributes aliasAttributes2 = new AliasAttributes(Constants.TEST_ALIAS, Constants.TEST_COMMAND);
        assertEquals(Constants.TEST_ALIAS, aliasAttributes2.getAlias());
        assertEquals(Constants.TEST_COMMAND, aliasAttributes2.getCommand());
    }

    @Test(expected = AssertionError.class)
    public void testNullAliasInConstructor() throws Exception {
        // The AliasAttributes constructor should reject any null arguments
        new AliasAttributes(null, Constants.TEST_COMMAND);
    }

    @Test(expected = AssertionError.class)
    public void testNullCommandInConstructor() throws Exception {
        // The AliasAttributes constructor should reject any null arguments
        new AliasAttributes(Constants.TEST_ALIAS, null);
    }

    @Test(expected = InvalidAliasParametersException.class)
    public void testInvalidAliasSave() throws Exception {
        // Create an AliasAttributes with an invalid alias (the alias should not be a command)
        AliasAttributes aliasAttributes = new AliasAttributes(Constants.TEST_COMMAND, Constants.TEST_COMMAND_2);
        aliasAttributes.save();
    }

    @Test(expected = InvalidAliasParametersException.class)
    public void testInvalidCommandSave() throws Exception {
        // Create an AliasAttributes with an invalid aliased command
        AliasAttributes aliasAttributes = new AliasAttributes(Constants.TEST_ALIAS, Constants.TEST_ALIAS);
        aliasAttributes.save();
    }

    @Test(expected = DuplicateAliasException.class)
    public void testDuplicateSave() throws Exception {
        // Create an AliasAttributes and perform the first save
        AliasAttributes aliasAttributes = new AliasAttributes(Constants.TEST_ALIAS, Constants.TEST_COMMAND);
        aliasAttributes.save();

        // Duplicate save should throw an exception
        aliasAttributes.save();
    }

    @Test
    public void testSave() throws Exception {
        AliasAttributes aliasAttributes = new AliasAttributes(Constants.TEST_ALIAS, Constants.TEST_COMMAND);

        // Make sure that the database is empty before save
        assertTrue(aliasDbInstance.getAll().isEmpty());

        // Command under test
        aliasAttributes.save();

        // Check that the alias has been persisted
        assertEquals(1, aliasDbInstance.getAll().size());
        assertEquals(Constants.TEST_COMMAND, aliasDbInstance.getCommandFromAlias(Constants.TEST_ALIAS));
    }

    @Test
    public void testToEntity() {
        // Create an AliasAttributes and turn it into an Alias entity
        AliasAttributes aliasAttributes = new AliasAttributes(Constants.TEST_ALIAS, Constants.TEST_COMMAND);
        Alias alias = aliasAttributes.toEntity();

        // Assert that the attributes remain the same
        assertEquals(aliasAttributes.getAlias(), alias.getAlias());
        assertEquals(aliasAttributes.getCommand(), alias.getCommand());
    }

}
