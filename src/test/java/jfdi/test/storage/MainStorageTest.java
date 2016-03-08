package jfdi.test.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import jfdi.storage.Constants;
import jfdi.storage.MainStorage;
import jfdi.storage.exceptions.ExistingFilesFoundException;

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
        File testDirectoryRootFile = testDirectoryRoot.toFile();
        FileUtils.deleteDirectory(testDirectoryRootFile);
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
        revertOriginalPreference(originalPreference);
        mainStorageInstance.removeInstance();
        if (testDirectoryFile.exists()) {
            FileUtils.deleteDirectory(testDirectoryFile);
        }
    }

    @Test
    public void testGetInstance() {
        // Test the ability to get an instance of MainStorage in setUp()
        assertTrue(mainStorageInstance instanceof MainStorage);

        // Make sure that getInstance returns the same instance in every call
        MainStorage mainStorageInstance2 = MainStorage.getInstance();
        assertSame(mainStorageInstance, mainStorageInstance2);
    }

    @Test
    public void testInitialize() throws Exception {
        initializeStorage();

        // Assert test folder exists after a successful initialization
        assertTrue(testDirectoryFile.exists());

        // Check that the file permissions are set correctly
        assertTrue(testDirectoryFile.canExecute());
        assertTrue(testDirectoryFile.canWrite());
    }

    @Test
    public void testSuccessfulLoad() {
        try {
            mainStorageInstance.load(testDirectoryString);

            // Assert test folder exists after a successful load
            assertTrue(testDirectoryFile.exists());

            // Check that the file permissions are set correctly
            assertTrue(testDirectoryFile.canExecute());
            assertTrue(testDirectoryFile.canWrite());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testLoadValidExistingFiles() {
        createValidDataFiles();

        try {
            mainStorageInstance.load(testDirectoryString);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test(expected = ExistingFilesFoundException.class)
    public void testLoadInvalidExistingFiles() throws Exception {
        createInvalidDataFiles();
        mainStorageInstance.load(testDirectoryString);
    }

    @Test(expected = IllegalAccessException.class)
    public void testChangeDirectoryBeforeLoad() throws Exception {
        mainStorageInstance.changeDirectory(testDirectoryString);
    }

    @Test(expected = ExistingFilesFoundException.class)
    public void testChangeDirectoryWithExistingInvalidFiles() throws Exception {
        initializeStorage();
        Path subdirectoryPath = Paths.get(testDirectoryString, Constants.TEST_SUBDIRECTORY_NAME);
        String subdirectoryString = subdirectoryPath.toString();
        TestHelper.createInvalidDataFiles(subdirectoryString);
        mainStorageInstance.changeDirectory(subdirectoryString);

        // Check that the path to the new directory is saved
        String preferredDirectory = mainStorageInstance.getPreferredDirectory();
        assertEquals(preferredDirectory, subdirectoryString);
    }

    @Test
    public void testChangeToNewDirectory() {
        initializeStorage();
        Path subdirectoryPath = Paths.get(testDirectoryString, Constants.TEST_SUBDIRECTORY_NAME);
        String subdirectoryString = subdirectoryPath.toString();
        try {
            mainStorageInstance.changeDirectory(subdirectoryString);

            // Check that the path to the new directory is saved
            String preferredDirectory = mainStorageInstance.getPreferredDirectory();
            assertEquals(preferredDirectory, subdirectoryString);
        } catch (Exception e) {
            fail(e.getMessage());
        }
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

    /**
     * This method reverts the preference file to its original form after using
     * it in the tests.
     *
     * @param originalPreference
     *            the original storage directory path
     */
    private void revertOriginalPreference(String originalPreference) {
        if (originalPreference == null) {
            FileUtils.deleteQuietly(Constants.PATH_PREFERENCE_FILE.toFile());
            return;
        }
        mainStorageInstance.setPreferredDirectory(originalPreference);
    }
}
