package jfdi.test.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

import jfdi.storage.Constants;
import jfdi.storage.FileManager;
import jfdi.storage.apis.MainStorage;
import jfdi.storage.exceptions.FilesReplacedException;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class FileManagerTest {

    private static Path testDirectoryRoot = null;
    private static MainStorage mainStorageInstance = null;
    private static String originalPreference = null;

    private Path testDirectoryPath = null;
    private File testDirectoryFile = null;
    private String testDirectoryString = null;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        testDirectoryRoot = Files.createTempDirectory(Constants.TEST_DIRECTORY_PREFIX);
        mainStorageInstance = MainStorage.getInstance();
        originalPreference = mainStorageInstance.getPreferredDirectory();
        mainStorageInstance.setPreferredDirectory(testDirectoryRoot.toString());
        mainStorageInstance.initialize();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        FileUtils.deleteDirectory(testDirectoryRoot.toFile());
        TestHelper.revertOriginalPreference(mainStorageInstance, originalPreference);
    }

    @Before
    public void setUp() throws Exception {
        testDirectoryPath = Paths.get(testDirectoryRoot.toString(), Constants.TEST_DIRECTORY_NAME);
        testDirectoryFile = testDirectoryPath.toFile();
        testDirectoryString = testDirectoryFile.getAbsolutePath();
        assertFalse(testDirectoryFile.exists());
    }

    @After
    public void tearDown() throws Exception {
        if (testDirectoryFile.exists()) {
            FileUtils.deleteDirectory(testDirectoryFile);
        }
    }

    @Test
    public void testPrepareDirectory() throws Exception {
        // The folder should not exist at the start
        assertFalse(testDirectoryFile.exists());

        // Command under test (we prepare the directory for storage)
        FileManager.prepareDirectory(testDirectoryString);

        // Now the folder should exist
        assertTrue(testDirectoryFile.exists());
    }

    @Test
    public void testMoveFilesToNewDirectory() throws Exception {
        // Create some valid data files and get their checksums
        mainStorageInstance.use(testDirectoryString);
        TestHelper.createValidDataFiles(testDirectoryString);
        HashMap<String, Long> checksums = TestHelper.getDataFileChecksums(testDirectoryString);
        String subdirectory = Paths.get(testDirectoryString, Constants.TEST_SUBDIRECTORY_NAME).toString();

        // Command under test (move the data files to the new directory)
        String dataPath = TestHelper.getDataDirectory(subdirectory);
        FileManager.moveFilesToDirectory(dataPath);

        // Assert that their checksums remain the same
        assertTrue(TestHelper.hasDataFileChecksums(subdirectory, checksums));
    }

    @Test(expected = FilesReplacedException.class)
    public void testMoveFilesToDirectoryWithExistingData() throws Exception {
        // Create some valid data files and get their checksums
        mainStorageInstance.use(testDirectoryString);
        TestHelper.createValidDataFiles(testDirectoryString);
        HashMap<String, Long> checksums = TestHelper.getDataFileChecksums(testDirectoryString);
        String subdirectory = Paths.get(testDirectoryString, Constants.TEST_SUBDIRECTORY_NAME).toString();

        // Create some invalid data files in the destination directory for collision
        TestHelper.createInvalidDataFiles(subdirectory);

        try {
            // Command under test (move the data files to the new directory)
            String dataPath = TestHelper.getDataDirectory(subdirectory);
            FileManager.moveFilesToDirectory(dataPath);
        } catch (FilesReplacedException e) {
            // Ensure that files are replaced and assert that the data files in
            // the destination remains the same
            assertTrue(TestHelper.hasDataFileChecksums(subdirectory, checksums));
            throw e;
        }
    }

    @Test
    public void testBackupAndRemove() {
        // Create the test file
        Path testFilePath = Paths.get(testDirectoryString, Constants.TEST_FILE_NAME);
        File testFile = testFilePath.toFile();
        testFile.getParentFile().mkdirs();
        FileManager.writeToFile(Constants.TEST_FILE_DATA, testFilePath);

        // Command under test (backup and remove the file)
        String backupPath = FileManager.backupAndRemove(testFilePath);

        // Assert that the test file has now been moved to the backup location
        File backupFile = new File(backupPath);
        assertFalse(testFile.exists());
        assertTrue(backupFile.exists());
    }

    @Test
    public void testWriteAndRead() {
        // Create the necessary directories
        Path filePath = Paths.get(testDirectoryString, Constants.TEST_FILE_NAME);
        File parentFile = filePath.getParent().toFile();
        parentFile.mkdirs();

        // Test writing to the file
        FileManager.writeToFile(Constants.TEST_FILE_DATA, filePath);

        // Test reading from the file
        String readString = FileManager.readFileToString(filePath);

        // Ensure that both are the same
        assertEquals(Constants.TEST_FILE_DATA, readString);
    }

}
