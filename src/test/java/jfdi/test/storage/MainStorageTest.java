//@@author A0121621Y
package jfdi.test.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import jfdi.storage.Constants;
import jfdi.storage.apis.MainStorage;
import jfdi.storage.apis.TaskDb;
import jfdi.storage.exceptions.FilesReplacedException;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class MainStorageTest {

    private static Path testDirectoryRoot = null;

    private MainStorage mainStorageInstance = null;
    private Path testDirectoryPath = null;
    private File testDirectoryFile = null;
    private String testDirectoryString = null;
    private String originalPreference = null;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        testDirectoryRoot = Files.createTempDirectory(Constants.TEST_DIRECTORY_PREFIX);
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        FileUtils.deleteDirectory(testDirectoryRoot.toFile());
    }

    @Before
    public void setUp() throws Exception {
        mainStorageInstance = MainStorage.getInstance();
        originalPreference = mainStorageInstance.getPreferredDirectory();
        testDirectoryPath = Paths.get(testDirectoryRoot.toString(), Constants.TEST_DIRECTORY_NAME);
        testDirectoryFile = testDirectoryPath.toFile();
        testDirectoryString = testDirectoryFile.getAbsolutePath();
        assertFalse(testDirectoryFile.exists());
    }

    @After
    public void tearDown() throws Exception {
        TestHelper.revertOriginalPreference(mainStorageInstance, originalPreference);
        mainStorageInstance.removeInstance();
        if (testDirectoryFile.exists()) {
            FileUtils.deleteDirectory(testDirectoryFile);
        }
    }

    @Test
    public void testGetInstance() {
        // Test the ability to get an instance of MainStorage in the test's setUp() method
        assertTrue(mainStorageInstance instanceof MainStorage);

        // Make sure that getInstance returns the same instance in every call
        assertSame(mainStorageInstance, MainStorage.getInstance());
    }

    @Test
    public void testInitialize() throws Exception {
        // Command under test
        initializeStorage();

        // Assert test folder exists after a successful initialization
        assertTrue(testDirectoryFile.exists());

        // Check that the file permissions are set correctly
        assertTrue(testDirectoryFile.canExecute());
        assertTrue(testDirectoryFile.canWrite());
    }

    @Test
    public void testUse() throws Exception {
        // Initialize storage and create a valid task file to load from
        initializeStorage();
        Path subdirectoryPath = Paths.get(testDirectoryString, Constants.TEST_SUBDIRECTORY_NAME);
        String subdirectoryString = subdirectoryPath.toString();
        TestHelper.createValidTaskFile(subdirectoryString);

        // There should be no tasks before we switch directory
        TaskDb.getInstance().reset();
        assertEquals(0, TaskDb.getInstance().getAll().size());

        // Command under test
        mainStorageInstance.use(subdirectoryString);

        // The preferred directory should be set as the subdirectory and there
        // should be 1 task loaded from the subdirectory
        assertEquals(subdirectoryString, mainStorageInstance.getPreferredDirectory());
        assertEquals(1, TaskDb.getInstance().getAll().size());
    }

    @Test
    public void testSuccessfulLoad() throws Exception {
        // Command under test
        mainStorageInstance.load(testDirectoryString);

        // Assert test folder exists after a successful load
        assertTrue(testDirectoryFile.exists());

        // Check that the file permissions are set correctly
        assertTrue(testDirectoryFile.canExecute());
        assertTrue(testDirectoryFile.canWrite());
    }

    @Test
    public void testLoadValidExistingFiles() throws Exception {
        // Create some valid data files to load from
        createValidDataFiles();

        // Command under test (there should be no exceptions thrown)
        mainStorageInstance.load(testDirectoryString);
    }

    @Test(expected = FilesReplacedException.class)
    public void testLoadInvalidExistingFiles() throws Exception {
        // Create some invalid data files to load from
        createInvalidDataFiles();

        // Loading the invalid data files should give an exception
        String dataDirectory = mainStorageInstance.getDataDirectory(testDirectoryString);
        mainStorageInstance.load(dataDirectory);
    }

    @Test(expected = AssertionError.class)
    public void testChangeDirectoryBeforeInitialization() throws Exception {
        // We should not be able to change directory before storage is initialized
        mainStorageInstance.changeDirectory(testDirectoryString);
    }

    @Test(expected = FilesReplacedException.class)
    public void testChangeDirectoryWithExistingInvalidFiles() throws Exception {
        // Initialized storage and create some valid data files to be moved
        initializeStorage();
        TestHelper.createValidDataFiles(testDirectoryString);

        // Create some invalid data files in the destination directory
        Path subdirectoryPath = Paths.get(testDirectoryString, Constants.TEST_SUBDIRECTORY_NAME);
        String subdirectoryString = subdirectoryPath.toString();
        TestHelper.createInvalidDataFiles(subdirectoryString);

        // An exception will be thrown when the invalid data files are replaced
        mainStorageInstance.changeDirectory(subdirectoryString);
    }

    @Test
    public void testChangeToNewDirectory() throws Exception {
        // Initialize storage and set up the paths to be used
        initializeStorage();
        Path subdirectoryPath = Paths.get(testDirectoryString, Constants.TEST_SUBDIRECTORY_NAME);
        String subdirectoryString = subdirectoryPath.toString();

        // Command under test (change to the new directory)
        mainStorageInstance.changeDirectory(subdirectoryString);

        // Check that the path to the new directory is saved and no exceptions are thrown
        String preferredDirectory = mainStorageInstance.getPreferredDirectory();
        assertEquals(preferredDirectory, subdirectoryString);
    }

    /**
     * This method initializes fileStorage with the test directory, then reverts
     * the preference file back to its original form.
     */
    private void initializeStorage() {
        mainStorageInstance.setPreferredDirectory(testDirectoryString);
        try {
            mainStorageInstance.initialize();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method creates valid data files in the test directory.
     */
    private void createValidDataFiles() {
        TestHelper.createValidDataFiles(testDirectoryString);
    }

    /**
     * This method creates invalid data files in the test directory.
     */
    private void createInvalidDataFiles() {
        TestHelper.createInvalidDataFiles(testDirectoryString);
    }
}
