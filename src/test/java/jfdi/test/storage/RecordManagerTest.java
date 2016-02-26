package jfdi.test.storage;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.ArrayList;

import jfdi.storage.Constants;
import jfdi.storage.RecordManager;
import jfdi.storage.exceptions.ExistingFilesFoundException;

import org.junit.BeforeClass;
import org.junit.Test;

public class RecordManagerTest {

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
        RecordManager.setAllFilePaths(testDirectoryString);

        // Test getting of all file paths
        ArrayList<Path> obtainedFilePaths = RecordManager.getAllFilePaths();
        ArrayList<Path> expectedFilePaths = new ArrayList<Path>();
        Path filePath;
        for (String filename : Constants.FILENAME_ARRAY) {
            filePath = Paths.get(testDirectoryString, filename);
            expectedFilePaths.add(filePath);
        }

        assertTrue(TestHelper.hasSameElements(expectedFilePaths, obtainedFilePaths));
    }

    @Test
    public void testLoadAllRecords() {
        // Test setting of all file paths
        RecordManager.setAllFilePaths(testDirectoryString);
        TestHelper.createValidDataFiles(testDirectoryString);
        try {
            RecordManager.loadAllRecords();
        } catch (ExistingFilesFoundException e) {
            fail(e.getMessage());
        }
    }

    @Test(expected = ExistingFilesFoundException.class)
    public void testLoadAllRecordsWithInvalidData() throws Exception {
        RecordManager.setAllFilePaths(testDirectoryString);
        TestHelper.createInvalidDataFiles(testDirectoryString);
        RecordManager.loadAllRecords();
    }

}
