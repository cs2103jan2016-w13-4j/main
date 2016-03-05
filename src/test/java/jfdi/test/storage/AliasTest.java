package jfdi.test.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import jfdi.storage.Constants;
import jfdi.storage.MainStorage;
import jfdi.storage.data.Alias;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

public class AliasTest {

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
        Alias.resetProgramStorage();
    }

    @Test
    public void testAliasConstructorAndGetters() {
        Alias alias = new Alias(Constants.TEST_ALIAS, Constants.TEST_COMMAND);
        assertEquals(alias.getAlias(), Constants.TEST_ALIAS);
        assertEquals(alias.getCommand(), Constants.TEST_COMMAND);
    }

    @Test
    public void testGetAllAliases() {
        Alias alias = new Alias(Constants.TEST_ALIAS, Constants.TEST_COMMAND);
        alias.createAndPersist();

        Alias alias2 = new Alias(Constants.TEST_ALIAS_2, Constants.TEST_COMMAND_2);
        alias2.createAndPersist();

        ArrayList<Alias> expectedAliasList = new ArrayList<Alias>();
        expectedAliasList.add(alias);
        expectedAliasList.add(alias2);

        ArrayList<Alias> obtainedAliasList = new ArrayList<Alias>(Alias.getAllAliases());
        assertTrue(TestHelper.hasSameElements(expectedAliasList, obtainedAliasList));
    }

    @Test
    public void testSetAndGetFilePath() {
        Path subdirectory = Paths.get(testDirectory.toString(), Constants.TEST_SUBDIRECTORY_NAME);
        Path expectedAliasPath = Paths.get(subdirectory.toString(), Constants.FILENAME_ALIAS);

        Alias.setFilePath(subdirectory.toString());

        assertEquals(expectedAliasPath, Alias.getFilePath());

        // Reset back to the original filepath
        Alias.setFilePath(testDirectoryString);
    }

    @Test
    public void testDestroyAndUndestroy() {
        Alias alias = new Alias(Constants.TEST_ALIAS, Constants.TEST_COMMAND);
        alias.createAndPersist();

        assertEquals(Alias.getCommandFromAlias(alias.getAlias()), alias.getCommand());

        Alias.destroy(alias.getAlias());

        assertEquals(Alias.getCommandFromAlias(alias.getAlias()), null);

        Alias.undestroy(alias.getAlias());

        assertEquals(Alias.getCommandFromAlias(alias.getAlias()), alias.getCommand());
    }

    @Test
    public void testCreateAndPersistAndLoad() {
        Alias alias = new Alias(Constants.TEST_ALIAS, Constants.TEST_COMMAND);
        alias.createAndPersist();

        assertEquals(Alias.getCommandFromAlias(alias.getAlias()), alias.getCommand());

        Alias.destroy(alias.getAlias());
        assertEquals(Alias.getCommandFromAlias(alias.getAlias()), null);

        Alias.load();
        assertEquals(Alias.getCommandFromAlias(alias.getAlias()), alias.getCommand());
    }

}
