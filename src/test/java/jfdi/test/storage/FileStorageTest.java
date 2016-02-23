package jfdi.test.storage;

import static org.junit.Assert.fail;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;

import java.io.File;
import java.io.PrintWriter;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import jfdi.storage.Constants;
import jfdi.storage.FileStorage;
import jfdi.storage.exceptions.ExistingFilesFoundException;

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
        createInvalidDataFiles(subdirectoryString);
        fileStorageInstance.changeDirectory(subdirectoryString);
    }

    @Test
    public void testChangeDirectoryWithExistingValidFiles() {
        initializeStorage();
        Path subdirectoryPath = Paths.get(testDirectoryString, Constants.TEST_SUBDIRECTORY_NAME);
        String subdirectoryString = subdirectoryPath.toString();
        createValidDataFiles(subdirectoryString);
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
        createValidDataFiles(testDirectoryString);
    }

    /**
     * This method creates valid data files in directoryPath
     *
     * @param directoryPath
     *            the directory to create the data files in
     */
    private void createValidDataFiles(String directoryPath) {
        createDataFilesWith(directoryPath, Constants.EMPTY_LIST_STRING);
    }

    /**
     * This method creates invalid data files in the test directory.
     */
    private void createInvalidDataFiles() {
        createInvalidDataFiles(testDirectoryString);
    }

    /**
     * This method creates invalid data files in directoryPath.
     *
     * @param directoryPath
     *            the directory to create the data files in
     */
    private void createInvalidDataFiles(String directoryPath) {
        createDataFilesWith(directoryPath, Constants.EMPTY_STRING);
    }

    /**
     * This method creates data files in the specified directoryPath with the
     * given data written into the data files.
     *
     * @param directoryPath
     *            the path to the directory in which we create the data files
     * @param data
     *            the content that is to be written to the data files
     */
    private void createDataFilesWith(String directoryPath, String data) {
        File parentDirectory = new File(directoryPath);
        parentDirectory.mkdirs();

        String[] dataFilenames = {Constants.FILENAME_TASK, Constants.FILENAME_ALIAS};
        Path dataPath;
        File dataFile;
        for (String dataFilename : dataFilenames) {
            dataPath = Paths.get(directoryPath, dataFilename);
            dataFile = dataPath.toFile();
            writeToFile(dataFile, data);
        }
    }

    /**
     * This method writes the given data to the given dataFile.
     *
     * @param dataFile
     *            the file in which data is to be written into
     * @param data
     *            the data which we want to write into dataFile
     */
    private void writeToFile(File dataFile, String data) {
        try {
            PrintWriter writer = new PrintWriter(dataFile, Constants.CHARSET);
            writer.println(data);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
