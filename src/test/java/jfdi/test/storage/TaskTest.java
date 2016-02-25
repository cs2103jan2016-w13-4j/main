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
import jfdi.storage.records.Task;

import org.junit.After;
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
        Task.resetProgramStorage();
    }

    @Test
    public void testSettersAndGetters() {
        Task task = new Task();
        task.setDescription(Constants.TEST_TASK_DESCRIPTION);
        task.setStartDateTime(Constants.TEST_TASK_STARTDATETIME);
        task.setEndDateTime(Constants.TEST_TASK_ENDDATETIME);
        task.setTags(Constants.TEST_TASK_TAG_1);

        TreeSet<Duration> reminders = new TreeSet<Duration>();
        reminders.add(Constants.TEST_TASK_REMINDER_DURATION);
        task.setReminders(reminders);

        task.setCompleted(true);

        assertEquals(task.getDescription(), Constants.TEST_TASK_DESCRIPTION);
        assertEquals(task.getStartDateTime(), Constants.TEST_TASK_STARTDATETIME);
        assertEquals(task.getEndDateTime(), Constants.TEST_TASK_ENDDATETIME);

        HashSet<String> tags = task.getTags();
        assertEquals(tags.size(), 1);
        assertTrue(tags.contains(Constants.TEST_TASK_TAG_1));
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

    @Test
    public void testRemoveTagFromNonExistentTask() {
        boolean isSuccessful = false;

        isSuccessful = Task.removeTagById(-1, Constants.TEST_TASK_TAG_1);
        assertFalse(isSuccessful);
    }

    @Test
    public void testRemoveNonExistentTag() {
        boolean isSuccessful = false;

        Task task = new Task();
        task.setTags(Constants.TEST_TASK_TAG_1);
        task.createAndPersist();

        isSuccessful = Task.removeTagById(task.getId(), Constants.TEST_TASK_TAG_2);
        assertFalse(isSuccessful);
    }

    @Test
    public void testRemoveTag() {
        boolean isSuccessful = false;

        Task task = new Task();
        task.setTags(Constants.TEST_TASK_TAG_1, Constants.TEST_TASK_TAG_2);
        task.createAndPersist();

        isSuccessful = Task.removeTagById(task.getId(), Constants.TEST_TASK_TAG_1);
        assertTrue(isSuccessful);

        HashSet<String> tags = task.getTags();
        assertFalse(tags.contains(Constants.TEST_TASK_TAG_1));
        assertTrue(tags.contains(Constants.TEST_TASK_TAG_2));
    }

    @Test
    public void testGetByTag() {
        Task task1 = new Task();
        task1.setTags(Constants.TEST_TASK_TAG_1);
        task1.createAndPersist();

        Task task2 = new Task();
        task2.setTags(Constants.TEST_TASK_TAG_2);
        task2.createAndPersist();

        Task task3 = new Task();
        task3.setTags(Constants.TEST_TASK_TAG_1, Constants.TEST_TASK_TAG_2);
        task3.createAndPersist();

        HashSet<Task> tasksWithTag1 = new HashSet<Task>();
        tasksWithTag1.add(task1);
        tasksWithTag1.add(task3);

        HashSet<Task> tasksWithTag2 = new HashSet<Task>();
        tasksWithTag2.add(task2);
        tasksWithTag2.add(task3);

        assertSame(Task.getByTag(null), null);
        assertTrue(Task.getByTag(Constants.TEST_TASK_DESCRIPTION).isEmpty());
        assertTrue(TestHelper.hasSameElements(tasksWithTag1, Task.getByTag(Constants.TEST_TASK_TAG_1)));
        assertTrue(TestHelper.hasSameElements(tasksWithTag2, Task.getByTag(Constants.TEST_TASK_TAG_2)));
    }

    @Test
    public void testAddTagById() {
        HashSet<String> tags = null;
        Task task = new Task();
        task.createAndPersist();

        tags = task.getTags();
        assertTrue(tags.isEmpty());

        boolean isAddTagToNullSuccessful = Task.addTagById(null, Constants.TEST_TASK_TAG_1);
        assertFalse(isAddTagToNullSuccessful);

        boolean isAddTagToNonExistentIdSuccessful = Task.addTagById(-1, Constants.TEST_TASK_TAG_1);
        assertFalse(isAddTagToNonExistentIdSuccessful);

        assertFalse(tags.contains(Constants.TEST_TASK_TAG_1));
        boolean isAddTagToExistentIdSuccessful = Task.addTagById(task.getId(), Constants.TEST_TASK_TAG_1);
        assertTrue(isAddTagToExistentIdSuccessful);
        assertTrue(tags.contains(Constants.TEST_TASK_TAG_1));

        boolean isAddDuplicateTagSuccessful = Task.addTagById(task.getId(), Constants.TEST_TASK_TAG_1);
        assertFalse(isAddDuplicateTagSuccessful);

        assertFalse(tags.contains(Constants.TEST_TASK_TAG_2));
        boolean isAddAnotherTagSuccessful = Task.addTagById(task.getId(), Constants.TEST_TASK_TAG_2);
        assertTrue(isAddAnotherTagSuccessful);
        assertTrue(tags.contains(Constants.TEST_TASK_TAG_2));
    }

}
