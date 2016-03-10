package jfdi.test.storage;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

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
        ArrayList<Path> expectedFilePaths = new ArrayList<Path>();
        Path filePath;
        for (String filename : Constants.FILENAME_DATA_ARRAY) {
            filePath = Paths.get(testDirectoryString, filename);
            expectedFilePaths.add(filePath);
        }

        assertTrue(TestHelper.hasSameElements(expectedFilePaths, obtainedFilePaths));
    }

    @Test
    public void testLoadAllDatabases() {
        // Test setting of all file paths
        DatabaseManager.setAllFilePaths(testDirectoryString);
        TestHelper.createValidDataFiles(testDirectoryString);
        try {
            DatabaseManager.loadAllDatabases();
        } catch (FilesReplacedException e) {
            fail(e.getMessage());
        }
    }

    @Test(expected = FilesReplacedException.class)
    public void testLoadAllRecordsWithInvalidData() throws Exception {
        DatabaseManager.setAllFilePaths(testDirectoryString);
        TestHelper.createInvalidDataFiles(testDirectoryString);
        DatabaseManager.loadAllDatabases();
    }

}
