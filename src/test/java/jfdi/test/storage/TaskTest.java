package jfdi.test.storage;

import static org.junit.Assert.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeSet;

import jfdi.storage.Constants;
import jfdi.storage.FileStorage;
import jfdi.storage.records.Alias;
import jfdi.storage.records.Task;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TaskTest {

    private static Path testDirectory = null;
    private static String testDirectoryString = null;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        testDirectory = Files.createTempDirectory(Constants.TEST_DIRECTORY_NAME);
        testDirectoryString = testDirectory.toString();
        FileStorage fileStorageInstance = FileStorage.getInstance();
        fileStorageInstance.load(testDirectory.toString());
    }

    @After
    public void tearDown() throws Exception {
        clearTaskList();
    }

    @Test
    public void testSettersAndGetters() {
        Task task = new Task();
        task.setDescription(Constants.TEST_TASK_DESCRIPTION);
        task.setStartDateTime(Constants.TEST_TASK_STARTDATETIME);
        task.setEndDateTime(Constants.TEST_TASK_ENDDATETIME);

        HashSet<String> tags = new HashSet<String>();
        tags.add(Constants.TEST_TASK_TAG_1);
        task.setTags(tags);

        TreeSet<Duration> reminders = new TreeSet<Duration>();
        reminders.add(Constants.TEST_TASK_REMINDER_DURATION);
        task.setReminders(reminders);

        task.setCompleted(true);

        assertEquals(task.getDescription(), Constants.TEST_TASK_DESCRIPTION);
        assertEquals(task.getStartDateTime(), Constants.TEST_TASK_STARTDATETIME);
        assertEquals(task.getEndDateTime(), Constants.TEST_TASK_ENDDATETIME);
        assertSame(task.getTags(), tags);
        assertSame(task.getReminders(), reminders);
        assertEquals(task.isCompleted(), true);
    }

    @Test
    public void testSetAndGetFilePath() {
        Path subdirectory = Paths.get(testDirectory.toString(), Constants.TEST_SUBDIRECTORY_NAME);
        Path expectedTaskPath = Paths.get(subdirectory.toString(), Constants.FILENAME_TASK);

        Task.setFilePath(subdirectory.toString());

        assertEquals(expectedTaskPath, Task.getFilePath());

        // Reset back to the original filepath
        Task.setFilePath(testDirectoryString);
    }

    @Test
    public void testGetAll() {
        Task task1 = new Task();
        Task task2 = new Task();
        task1.createAndPersist();
        task2.createAndPersist();

        ArrayList<Task> expectedTaskList = new ArrayList<Task>();
        expectedTaskList.add(task1);
        expectedTaskList.add(task2);

        assertTrue(TestHelper.hasSameElements(expectedTaskList, Task.getAll()));
    }

    @Test
    public void testGetById() {
        Task task = new Task();
        task.createAndPersist();
        assertSame(Task.getById(task.getId()), task);
    }

    @Test
    public void testMarkAsCompleteAndIncomplete() {
        Task task = new Task();
        task.createAndPersist();

        // Testing mark as complete
        Task.markAsComplete(task.getId());

        assertTrue(task.isCompleted());

        // Testing mark as incomplete
        Task.markAsIncomplete(task.getId());

        assertFalse(task.isCompleted());
    }

    @Test
    public void testDestroyAndUndestroy() {
        Task task = new Task();
        task.createAndPersist();

        assertSame(Task.getById(task.getId()), task);

        // Testing destroy
        Task.destroy(task.getId());
        assertSame(Task.getById(task.getId()), null);

        // Testing undestroy
        Task.undestroy(task.getId());
        assertSame(Task.getById(task.getId()), task);
    }

    @Test
    public void testCreateAndPersistAndLoad() {
        Task task = new Task();
        task.setDescription(Constants.TEST_TASK_DESCRIPTION);
        task.createAndPersist();

        // Testing create
        assertSame(Task.getById(task.getId()), task);

        Task.destroy(task.getId());

        assertSame(Task.getById(task.getId()), null);

        // Testing persistence and load
        Task.load();
        // There should only be 1 task loaded
        Task retrievedTask = Task.getById(1);
        assertEquals(retrievedTask.getDescription(), Constants.TEST_TASK_DESCRIPTION);
    }

    private void clearTaskList() {
        ArrayList<Task> taskList = new ArrayList<Task>(Task.getAll());
        for (Task task : taskList) {
            Task.destroy(task.getId());
        }
    }

}
