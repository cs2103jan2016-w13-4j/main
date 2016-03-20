package jfdi.test.storage;

import static org.junit.Assert.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

import jfdi.storage.Constants;
import jfdi.storage.DatabaseManager;
import jfdi.storage.exceptions.FilesReplacedException;

import org.junit.BeforeClass;
import org.junit.Test;

public class DatabaseManagerTest {

    private static Path testDirectory = null;
    private static String testDirectoryString = null;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        testDirectory = Files.createTempDirectory(Constants.TEST_DIRECTORY_NAME);
        testDirectoryString = testDirectory.toString();
    }

    @Test
    public void testSetAndGetAllFilePaths() {
        // Test setting of all file paths
        DatabaseManager.setAllFilePaths(testDirectoryString);

        // Test getting of all file paths
        ArrayList<Path> obtainedFilePaths = DatabaseManager.getAllFilePaths();

        // Assert that it contains all the file paths we expect
        ArrayList<Path> expectedFilePaths = new ArrayList<Path>();
        Arrays.stream(Constants.FILENAME_DATA_ARRAY).map(filename -> {
            return Paths.get(testDirectoryString, filename);
        }).forEach(expectedFilePaths::add);
        assertTrue(TestHelper.hasSameElements(expectedFilePaths, obtainedFilePaths));
    }

    @Test
    public void testLoadAllDatabases() throws Exception {
        // Set all the file paths
        String dataPath = TestHelper.getDataDirectory(testDirectoryString);
        DatabaseManager.setAllFilePaths(dataPath);

        // Create some valid data files to load from
        TestHelper.createValidDataFiles(testDirectoryString);

        // Command under test (load from the valid data files)
        DatabaseManager.loadAllDatabases();
    }

    @Test(expected = FilesReplacedException.class)
    public void testLoadAllRecordsWithInvalidData() throws Exception {
        // Set all the file paths
        String dataPath = TestHelper.getDataDirectory(testDirectoryString);
        DatabaseManager.setAllFilePaths(dataPath);

        // Create some invalid data files to load from
        TestHelper.createInvalidDataFiles(testDirectoryString);

        // Command under test (load from the invalid data files)
        DatabaseManager.loadAllDatabases();
    }

}
