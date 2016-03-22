package jfdi.test.storage;

import static org.junit.Assert.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;

import jfdi.storage.Constants;
import jfdi.storage.apis.MainStorage;
import jfdi.storage.apis.TaskAttributes;
import jfdi.storage.apis.TaskDb;
import jfdi.storage.exceptions.FilePathPair;
import jfdi.storage.exceptions.InvalidIdException;
import jfdi.storage.exceptions.NoAttributesChangedException;
import jfdi.storage.serializer.Serializer;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class TaskDbTest {

    private static Path testDirectory = null;
    private static String testDirectoryString = null;
    private static TaskDb taskDbInstance = null;
    private static MainStorage mainStorageInstance = null;
    private static String originalPreference = null;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        testDirectory = Files.createTempDirectory(Constants.TEST_DIRECTORY_NAME);
        testDirectoryString = testDirectory.toString();
        mainStorageInstance = MainStorage.getInstance();
        originalPreference = mainStorageInstance.getPreferredDirectory();
        mainStorageInstance.setPreferredDirectory(testDirectoryString);
        mainStorageInstance.initialize();
        mainStorageInstance.use(testDirectoryString);
        taskDbInstance = TaskDb.getInstance();
    }

    @AfterClass
    public static void tearDownAfterClass() {
        TestHelper.revertOriginalPreference(mainStorageInstance, originalPreference);
    }

    @After
    public void tearDown() throws Exception {
        taskDbInstance.reset();
    }

    @Test
    public void testCreate() throws Exception {
        TaskAttributes taskAttributes = new TaskAttributes();
        taskAttributes.setDescription(Constants.TEST_TASK_DESCRIPTION_1);

        // The database should be empty prior to the task creation
        assertTrue(taskDbInstance.getAll().isEmpty());

        // Command under test
        taskDbInstance.createOrUpdate(taskAttributes);

        // Check that the task has been created
        assertEquals(1, taskDbInstance.getAll().size());
        assertEquals(Constants.TEST_TASK_DESCRIPTION_1,
                taskDbInstance.getById(taskAttributes.getId()).getDescription());
    }

    @Test
    public void testUpdate() throws Exception {
        // Create a task
        TaskAttributes taskAttributes = new TaskAttributes();
        taskAttributes.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
        taskDbInstance.createOrUpdate(taskAttributes);

        // Check that the task has been created
        assertEquals(1, taskDbInstance.getAll().size());
        assertEquals(Constants.TEST_TASK_DESCRIPTION_1, taskDbInstance.getById(1).getDescription());

        // Command under test (update the task's description)
        taskAttributes.setDescription(Constants.TEST_TASK_DESCRIPTION_2);
        taskDbInstance.createOrUpdate(taskAttributes);

        // Ensure that object update (and not creation) takes place
        assertEquals(1, taskDbInstance.getAll().size());
        assertEquals(Constants.TEST_TASK_DESCRIPTION_2,
                taskDbInstance.getById(taskAttributes.getId()).getDescription());
    }

    @Test(expected = NoAttributesChangedException.class)
    public void testNoChangesUpdate() throws Exception {
        // Create a task
        TaskAttributes taskAttributes = new TaskAttributes();
        taskAttributes.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
        taskDbInstance.createOrUpdate(taskAttributes);

        // The second no-changes update triggers the exception
        taskDbInstance.createOrUpdate(taskAttributes);
    }

    @Test(expected = InvalidIdException.class)
    public void testInvalidIdUpdate() throws Exception {
        // Set the ID of a TaskAttributes to an invalid one
        TaskAttributes taskAttributes = new TaskAttributes();
        taskAttributes.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
        taskAttributes.setId(1);

        // Command under test (invalid ID will trigger exception)
        taskDbInstance.createOrUpdate(taskAttributes);
    }

    @Test
    public void testSetAndGetFilePath() {
        // Save the original file path so that we can revert it later
        Path originalFilePath = taskDbInstance.getFilePath();
        Path subdirectory = Paths.get(testDirectory.toString(), Constants.TEST_SUBDIRECTORY_NAME);
        Path expectedTaskPath = Paths.get(subdirectory.toString(), Constants.FILENAME_TASK);

        // Test setFilePath
        taskDbInstance.setFilePath(subdirectory.toString());

        // Assert that we get back the same file path
        assertEquals(expectedTaskPath, taskDbInstance.getFilePath());

        // Reset back to the original file path
        taskDbInstance.setFilePath(originalFilePath.getParent().toString());
    }

    @Test
    public void testGetAll() throws Exception {
        // Create the first task
        TaskAttributes taskAttributes = new TaskAttributes();
        taskAttributes.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
        taskAttributes.save();

        // A hack to create another identical task
        taskAttributes.setId(null);
        taskAttributes.save();

        // Check that the stored tasks are as expected
        ArrayList<TaskAttributes> taskAttributesList = new ArrayList<TaskAttributes>(taskDbInstance.getAll());
        assertEquals(2, taskAttributesList.size());
        assertEquals(Constants.TEST_TASK_DESCRIPTION_1, taskAttributesList.get(0).getDescription());
        assertEquals(Constants.TEST_TASK_DESCRIPTION_1, taskAttributesList.get(1).getDescription());
    }

    @Test
    public void testGetById() throws Exception {
        // Create a task
        TaskAttributes taskAttributes = new TaskAttributes();
        taskAttributes.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
        taskAttributes.save();

        // Check that we can get the same task using getById
        assertEquals(Constants.TEST_TASK_DESCRIPTION_1,
                taskDbInstance.getById(taskAttributes.getId()).getDescription());
    }

    @Test
    public void testHasId() throws Exception {
        // No task yet, so any ID should be invalid
        assertFalse(taskDbInstance.hasId(1));

        // Create a task
        TaskAttributes taskAttributes = new TaskAttributes();
        taskAttributes.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
        taskAttributes.save();

        // Now taskAttribute's ID should exist
        assertTrue(taskDbInstance.hasId(taskAttributes.getId()));
    }

    @Test
    public void testMarkAsCompleteAndIncomplete() throws Exception {
        // Create a task
        TaskAttributes taskAttributes = new TaskAttributes();
        taskAttributes.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
        taskAttributes.save();

        // Test markAsComplete
        taskDbInstance.markAsComplete(taskAttributes.getId());
        assertTrue(taskDbInstance.getById(taskAttributes.getId()).isCompleted());

        // Test markAsIncomplete
        taskDbInstance.markAsIncomplete(1);
        assertFalse(taskDbInstance.getById(taskAttributes.getId()).isCompleted());
    }

    @Test(expected = NoAttributesChangedException.class)
    public void testMarkCompletedAsComplete() throws Exception {
        // Create a task
        TaskAttributes taskAttributes = new TaskAttributes();
        taskAttributes.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
        taskAttributes.save();

        // Mark the task as completed
        taskAttributes.setCompleted(true);
        taskAttributes.save();

        // Mark it as complete again (this should trigger an exception)
        taskDbInstance.markAsComplete(taskAttributes.getId());
    }

    @Test(expected = NoAttributesChangedException.class)
    public void testMarkIncompletedAsIncomplete() throws Exception {
        // Create a task
        TaskAttributes taskAttributes = new TaskAttributes();
        taskAttributes.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
        taskAttributes.save();

        // Mark an incomplete task as incomplete (this should trigger an exception)
        taskDbInstance.markAsIncomplete(taskAttributes.getId());
    }

    @Test(expected = InvalidIdException.class)
    public void testMarkInvalidIdAsComplete() throws Exception {
        // Mark an invalid ID as complete
        taskDbInstance.markAsComplete(Integer.MAX_VALUE);
    }

    @Test(expected = InvalidIdException.class)
    public void testMarkInvalidIdAsIncomplete() throws Exception {
        // Mark an invalid ID as incomplete
        taskDbInstance.markAsIncomplete(Integer.MAX_VALUE);
    }

    @Test
    public void testDestroyAndUndestroy() throws Exception {
        // Create a task
        TaskAttributes taskAttributes = new TaskAttributes();
        taskAttributes.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
        taskAttributes.save();

        // Check that it has been created
        assertEquals(1, taskDbInstance.getAll().size());

        // Destroy the task
        taskDbInstance.destroy(taskAttributes.getId());
        assertEquals(0, taskDbInstance.getAll().size());

        // Undestroy the task
        taskDbInstance.undestroy(taskAttributes.getId());
        assertEquals(1, taskDbInstance.getAll().size());
    }

    @Test(expected = InvalidIdException.class)
    public void testInvalidIdDestroy() throws Exception {
        // Destroy an invalid ID
        taskDbInstance.destroy(Integer.MAX_VALUE);
    }

    @Test(expected = InvalidIdException.class)
    public void testInvalidIdUndestroy() throws Exception {
        // Undestroy an invalid ID
        taskDbInstance.undestroy(Integer.MAX_VALUE);
    }

    @Test
    public void testLoad() throws Exception {
        // Create a task
        TaskAttributes taskAttributes = new TaskAttributes();
        taskAttributes.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
        taskAttributes.save();

        // Get the JSON form of the current state
        String json = Serializer.serialize(taskDbInstance.getAll());

        // Remove the task
        taskDbInstance.destroy(taskAttributes.getId());
        assertTrue(taskDbInstance.getAll().isEmpty());

        // Create the data file and load from it
        TestHelper.createTaskFileWith(testDirectoryString, json);
        FilePathPair filesReplaced = taskDbInstance.load();

        // No files should have been replaced
        assertNull(filesReplaced);

        // Check that the original task exists after load
        assertEquals(1, taskDbInstance.getAll().size());
        TaskAttributes retrievedTaskAttributes = taskDbInstance.getById(taskAttributes.getId());
        assertEquals(Constants.TEST_TASK_DESCRIPTION_1, retrievedTaskAttributes.getDescription());
    }

    @Test
    public void testLoadFromInvalidData() throws Exception {
        // Create an invalid task file to load from
        TestHelper.createInvalidTaskFile(testDirectoryString);

        // Command under test (load from the invalid task file)
        FilePathPair filesReplaced = taskDbInstance.load();

        // Assert the files have been replaced
        assertNotNull(filesReplaced);
    }

    @Test(expected = InvalidIdException.class)
    public void testRemoveTagFromNonExistentTask() throws Exception {
        // Remove tag from a non-existent ID
        taskDbInstance.removeTagById(Integer.MAX_VALUE, Constants.TEST_TASK_TAG_1);
    }

    @Test(expected = NoAttributesChangedException.class)
    public void testRemoveNonExistentTag() throws Exception {
        // Create a task
        TaskAttributes taskAttributes = new TaskAttributes();
        taskAttributes.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
        taskAttributes.save();

        // Remove a non-existent tag
        taskDbInstance.removeTagById(taskAttributes.getId(), Constants.TEST_TASK_TAG_1);
    }

    @Test
    public void testRemoveTag() throws Exception {
        // Create a task
        TaskAttributes taskAttributes = new TaskAttributes();
        taskAttributes.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
        taskAttributes.setTags(Constants.TEST_TASK_TAG_1, Constants.TEST_TASK_TAG_2);
        taskAttributes.save();

        // Remove the first tag
        taskDbInstance.removeTagById(taskAttributes.getId(), Constants.TEST_TASK_TAG_1);

        // Ensure that the correct tag has been removed
        assertFalse(taskDbInstance.getById(taskAttributes.getId()).hasTag(Constants.TEST_TASK_TAG_1));
        assertTrue(taskDbInstance.getById(taskAttributes.getId()).hasTag(Constants.TEST_TASK_TAG_2));
    }

    @Test
    public void testGetByTag() throws Exception {
        // Create task 1 with tag 1
        TaskAttributes taskAttributes = new TaskAttributes();
        taskAttributes.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
        taskAttributes.setTags(Constants.TEST_TASK_TAG_1);
        taskAttributes.save();

        // Create task 2 with tag 2
        taskAttributes.setId(null);
        taskAttributes.setTags(Constants.TEST_TASK_TAG_2);
        taskAttributes.save();

        // Create task 3 with tag 2
        taskAttributes.setId(null);
        taskAttributes.setTags(Constants.TEST_TASK_TAG_2);
        taskAttributes.save();

        // Check that we have the correct number of tasks with the respective tags
        assertEquals(1, taskDbInstance.getByTag(Constants.TEST_TASK_TAG_1).size());
        assertEquals(2, taskDbInstance.getByTag(Constants.TEST_TASK_TAG_2).size());
    }

    @Test
    public void testAddTagById() throws Exception {
        // Create task with no tags
        TaskAttributes taskAttributes = new TaskAttributes();
        taskAttributes.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
        taskAttributes.save();

        // Add a tag to it and check that it's persisted
        taskDbInstance.addTagById(taskAttributes.getId(), Constants.TEST_TASK_TAG_1);
        assertTrue(taskDbInstance.getById(taskAttributes.getId()).hasTag(Constants.TEST_TASK_TAG_1));
    }

    @Test(expected = AssertionError.class)
    public void testAddTagToNull() throws Exception {
        // Add tag to a null ID
        taskDbInstance.addTagById(null, Constants.TEST_TASK_TAG_1);
    }

    @Test(expected = InvalidIdException.class)
    public void testAddTagToInvalidId() throws Exception {
        // Add tag to an invalid ID
        taskDbInstance.addTagById(Integer.MAX_VALUE, Constants.TEST_TASK_TAG_1);
    }

    @Test(expected = NoAttributesChangedException.class)
    public void testAddDuplicateTag() throws Exception {
        // Create task with tag 1
        TaskAttributes taskAttributes = new TaskAttributes();
        taskAttributes.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
        taskAttributes.setTags(Constants.TEST_TASK_TAG_1);
        taskAttributes.save();

        // Add the same tag to it
        taskDbInstance.addTagById(taskAttributes.getId(), Constants.TEST_TASK_TAG_1);
    }

    @Test
    public void testAddReminderById() throws Exception {
        // Create task with no reminders
        TaskAttributes taskAttributes = new TaskAttributes();
        taskAttributes.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
        taskAttributes.save();

        // Add a reminder to it and check that it's persisted
        taskDbInstance.addReminderById(taskAttributes.getId(), Constants.TEST_TASK_REMINDER_DURATION_1);
        TaskAttributes taskAttributes2 = taskDbInstance.getById(taskAttributes.getId());
        ArrayList<Duration> persistedReminderList = new ArrayList<Duration>(taskAttributes2.getReminders());
        assertTrue(persistedReminderList.contains(Constants.TEST_TASK_REMINDER_DURATION_1));
    }

    @Test(expected = AssertionError.class)
    public void testAddReminderToNull() throws Exception {
        // Add a reminder to null ID
        taskDbInstance.addReminderById(null, Constants.TEST_TASK_REMINDER_DURATION_1);
    }

    @Test(expected = InvalidIdException.class)
    public void testAddReminderToInvalidId() throws Exception {
        // Add a reminder to an invalid ID
        taskDbInstance.addReminderById(Integer.MAX_VALUE, Constants.TEST_TASK_REMINDER_DURATION_1);
    }

    @Test(expected = NoAttributesChangedException.class)
    public void testAddDuplicateReminder() throws Exception {
        // Create task with reminder 1
        TaskAttributes taskAttributes = new TaskAttributes();
        taskAttributes.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
        taskAttributes.setReminders(Constants.TEST_TASK_REMINDER_DURATION_1);
        taskAttributes.save();

        // Add the same reminder to it
        taskDbInstance.addReminderById(taskAttributes.getId(), Constants.TEST_TASK_REMINDER_DURATION_1);
    }

    @Test
    public void testTaskIdsResetUponLoad() throws Exception {
        // Create task 1
        TaskAttributes taskAttributes = new TaskAttributes();
        taskAttributes.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
        taskAttributes.save();

        // Create task 2
        taskAttributes.setId(null);
        taskAttributes.save();

        // Delete the first task
        taskDbInstance.destroy(1);

        // Command under test
        taskDbInstance.load();

        // There should only be one task with ID of 1
        assertTrue(taskDbInstance.hasId(1));
        assertFalse(taskDbInstance.hasId(2));
    }

}
