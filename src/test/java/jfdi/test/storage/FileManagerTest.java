package jfdi.test.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
    public void testPrepareDirectory() {
        assertFalse(testDirectoryFile.exists());

        FileManager.prepareDirectory(testDirectoryString);

        assertTrue(testDirectoryFile.exists());
    }

    @Test
    public void testMoveFilesToNewDirectory() {
        MainStorage fileStorageInstance = MainStorage.getInstance();
        try {
            fileStorageInstance.load(testDirectoryString);
            TestHelper.createValidDataFiles(testDirectoryString);
            Path subdirectory = Paths.get(testDirectoryString, Constants.TEST_SUBDIRECTORY_NAME);
            String subdirectoryString = subdirectory.toString();

            FileManager.moveFilesToDirectory(subdirectoryString);

            assertTrue(TestHelper.hasIdenticalDataFiles(testDirectoryString, subdirectoryString));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test(expected = FilesReplacedException.class)
    public void testMoveFilesToDirectoryWithExistingData() throws Exception {
        MainStorage fileStorageInstance = MainStorage.getInstance();
        fileStorageInstance.load(testDirectoryString);
        TestHelper.createValidDataFiles(testDirectoryString);
        Path subdirectory = Paths.get(testDirectoryString, Constants.TEST_SUBDIRECTORY_NAME);
        String subdirectoryString = subdirectory.toString();
        TestHelper.createInvalidDataFiles(subdirectoryString);

        try {
            FileManager.moveFilesToDirectory(subdirectoryString);
        } catch (FilesReplacedException e) {
            assertTrue(TestHelper.hasIdenticalDataFiles(testDirectoryString, subdirectoryString));
            throw e;
        }
    }

    @Test
    public void testBackupAndRemove() {
        Path testFilePath = Paths.get(testDirectoryString, Constants.TEST_FILE_NAME);
        File testFile = testFilePath.toFile();
        testFile.getParentFile().mkdirs();
        FileManager.writeToFile(Constants.TEST_FILE_DATA, testFilePath);
        String backupPath = FileManager.backupAndRemove(testFilePath);
        File backupFile = new File(backupPath);
        assertFalse(testFile.exists());
        assertTrue(backupFile.exists());
    }

    @Test
    public void testWriteAndRead() {
        Path filePath = Paths.get(testDirectoryString, Constants.TEST_FILE_NAME);
        File parentFile = filePath.getParent().toFile();
        parentFile.mkdirs();

        FileManager.writeToFile(Constants.TEST_FILE_DATA, filePath);
        String readString = FileManager.readFileToString(filePath);

        assertEquals(readString, Constants.TEST_FILE_DATA);
    }

}
