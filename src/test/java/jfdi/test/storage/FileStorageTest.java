package jfdi.test.storage;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import jfdi.storage.Constants;
import jfdi.storage.FileStorage;
import jfdi.storage.exceptions.ExistingFilesFoundException;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class FileStorageTest {

    private static Path testDirectoryRoot = null;

    private FileStorage fileStorageInstance = null;
    private Path testDirectoryPath = null;
    private File testDirectoryFile = null;
    private String testDirectoryString = null;

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
        fileStorageInstance = FileStorage.getInstance();
        testDirectoryPath = Paths.get(testDirectoryRoot.toString(), Constants.TEST_DIRECTORY_NAME);
        testDirectoryFile = testDirectoryPath.toFile();
        testDirectoryString = testDirectoryFile.getAbsolutePath();
        assertFalse(testDirectoryFile.exists());
    }

    @After
    public void tearDown() throws Exception {
        fileStorageInstance.removeInstance();
        if (testDirectoryFile.exists()) {
            FileUtils.deleteDirectory(testDirectoryFile);
        }
    }

    @Test
    public void testGetInstance() {
        // Test the ability to get an instance of FileStorage in setUp()
        assertTrue(fileStorageInstance instanceof FileStorage);

        // Make sure that getInstance returns the same instance in every call
        FileStorage fileStorageInstance2 = FileStorage.getInstance();
        assertSame(fileStorageInstance, fileStorageInstance2);
    }

    @Test
    public void testSuccessfulLoad() {
        try {
            fileStorageInstance.load(testDirectoryString);

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
            fileStorageInstance.load(testDirectoryString);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test(expected = ExistingFilesFoundException.class)
    public void testLoadInvalidExistingFiles() throws Exception {
        createInvalidDataFiles();
        fileStorageInstance.load(testDirectoryString);
    }

    @Test(expected = IllegalAccessException.class)
    public void testChangeDirectoryBeforeLoad() throws Exception {
        fileStorageInstance.changeDirectory(testDirectoryString);
    }

    @Test(expected = ExistingFilesFoundException.class)
    public void testChangeDirectoryWithExistingInvalidFiles() throws Exception {
        initializeStorage();
        Path subdirectoryPath = Paths.get(testDirectoryString, Constants.TEST_SUBDIRECTORY_NAME);
        String subdirectoryString = subdirectoryPath.toString();
        TestHelper.createInvalidDataFiles(subdirectoryString);
        fileStorageInstance.changeDirectory(subdirectoryString);
    }

    @Test
    public void testChangeDirectoryWithExistingValidFiles() {
        initializeStorage();
        Path subdirectoryPath = Paths.get(testDirectoryString, Constants.TEST_SUBDIRECTORY_NAME);
        String subdirectoryString = subdirectoryPath.toString();
        TestHelper.createValidDataFiles(subdirectoryString);
        try {
            fileStorageInstance.changeDirectory(subdirectoryString);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testChangeToNewDirectory() {
        initializeStorage();
        Path subdirectoryPath = Paths.get(testDirectoryString, Constants.TEST_SUBDIRECTORY_NAME);
        String subdirectoryString = subdirectoryPath.toString();
        try {
            fileStorageInstance.changeDirectory(subdirectoryString);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * This method initializes fileStorage with the test directory.
     */
    private void initializeStorage() {
        try {
            fileStorageInstance.load(testDirectoryString);
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
