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
import jfdi.storage.exceptions.InvalidIdException;
import jfdi.storage.exceptions.NoAttributesChangedException;
import jfdi.storage.serializer.Serializer;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

public class TaskDbTest {

    private static Path testDirectory = null;
    private static String testDirectoryString = null;
    private static TaskDb taskDbInstance = null;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        testDirectory = Files.createTempDirectory(Constants.TEST_DIRECTORY_NAME);
        testDirectoryString = testDirectory.toString();
        MainStorage fileStorageInstance = MainStorage.getInstance();
        fileStorageInstance.load(testDirectory.toString());
        taskDbInstance = TaskDb.getInstance();
    }

    @After
    public void tearDown() throws Exception {
        taskDbInstance.resetProgramStorage();
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
        assertEquals(taskDbInstance.getAll().size(), 1);
        assertEquals(taskDbInstance.getById(taskAttributes.getId()).getDescription(),
                Constants.TEST_TASK_DESCRIPTION_1);
    }

    @Test
    public void testUpdate() throws Exception {
        TaskAttributes taskAttributes = new TaskAttributes();
        taskAttributes.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
        taskDbInstance.createOrUpdate(taskAttributes);

        // Check that the task has been created
        assertEquals(taskDbInstance.getAll().size(), 1);
        assertEquals(taskDbInstance.getById(1).getDescription(), Constants.TEST_TASK_DESCRIPTION_1);

        // Update the task's description
        taskAttributes.setDescription(Constants.TEST_TASK_DESCRIPTION_2);
        taskDbInstance.createOrUpdate(taskAttributes);

        // Ensure that object update (and not creation) takes place
        assertEquals(taskDbInstance.getAll().size(), 1);
        assertEquals(taskDbInstance.getById(taskAttributes.getId()).getDescription(),
                Constants.TEST_TASK_DESCRIPTION_2);
    }

    @Test(expected = NoAttributesChangedException.class)
    public void testNoChangesUpdate() throws Exception {
        TaskAttributes taskAttributes = new TaskAttributes();
        taskAttributes.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
        taskDbInstance.createOrUpdate(taskAttributes);

        // The second no-changes update triggers the
        // NoAttributesChangedException
        taskDbInstance.createOrUpdate(taskAttributes);
    }

    @Test(expected = InvalidIdException.class)
    public void testInvalidIdUpdate() throws Exception {
        TaskAttributes taskAttributes = new TaskAttributes();
        taskAttributes.setDescription(Constants.TEST_TASK_DESCRIPTION_1);

        // The invalid ID will trigger InvalidIdException
        taskAttributes.setId(1);
        taskDbInstance.createOrUpdate(taskAttributes);
    }

    @Test
    public void testSetAndGetFilePath() {
        Path subdirectory = Paths.get(testDirectory.toString(), Constants.TEST_SUBDIRECTORY_NAME);
        Path expectedTaskPath = Paths.get(subdirectory.toString(), Constants.FILENAME_TASK);

        taskDbInstance.setFilePath(subdirectory.toString());

        assertEquals(expectedTaskPath, taskDbInstance.getFilePath());

        // Reset back to the original file path
        taskDbInstance.setFilePath(testDirectoryString);
    }

    @Test
    public void testGetAll() throws Exception {
        TaskAttributes taskAttributes = new TaskAttributes();
        taskAttributes.setDescription(Constants.TEST_TASK_DESCRIPTION_1);

        // Create the first task
        taskAttributes.save();

        // A hack to create another identical task
        taskAttributes.setId(null);
        taskAttributes.save();

        // Check that the stored tasks are as expected
        ArrayList<TaskAttributes> taskAttributesList = new ArrayList<TaskAttributes>(taskDbInstance.getAll());
        assertEquals(taskAttributesList.size(), 2);
        assertEquals(taskAttributesList.get(0).getDescription(), Constants.TEST_TASK_DESCRIPTION_1);
        assertEquals(taskAttributesList.get(1).getDescription(), Constants.TEST_TASK_DESCRIPTION_1);
    }

    @Test
    public void testGetById() throws Exception {
        // Create a task
        TaskAttributes taskAttributes = new TaskAttributes();
        taskAttributes.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
        taskAttributes.save();

        // Check that we can get the same task
        assertEquals(taskDbInstance.getById(taskAttributes.getId()).getDescription(),
                Constants.TEST_TASK_DESCRIPTION_1);
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

        // Mark it as complete again
        taskDbInstance.markAsComplete(taskAttributes.getId());
    }

    @Test(expected = NoAttributesChangedException.class)
    public void testMarkIncompletedAsIncomplete() throws Exception {
        // Create a task
        TaskAttributes taskAttributes = new TaskAttributes();
        taskAttributes.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
        taskAttributes.save();

        // Mark an incomplete task as incomplete
        taskDbInstance.markAsIncomplete(taskAttributes.getId());
    }

    @Test(expected = InvalidIdException.class)
    public void testMarkInvalidIdAsComplete() throws Exception {
        taskDbInstance.markAsComplete(1);
    }

    @Test(expected = InvalidIdException.class)
    public void testMarkInvalidIdAsIncomplete() throws Exception {
        taskDbInstance.markAsIncomplete(1);
    }

    @Test
    public void testDestroyAndUndestroy() throws Exception {
        // Create a task
        TaskAttributes taskAttributes = new TaskAttributes();
        taskAttributes.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
        taskAttributes.save();

        // Check that it has been created
        assertEquals(taskDbInstance.getAll().size(), 1);

        // Destroy the task
        taskDbInstance.destroy(taskAttributes.getId());
        assertEquals(taskDbInstance.getAll().size(), 0);

        // Undestroy the task
        taskDbInstance.undestroy(taskAttributes.getId());
        assertEquals(taskDbInstance.getAll().size(), 1);
    }

    @Test(expected = InvalidIdException.class)
    public void testInvalidIdDestroy() throws Exception {
        taskDbInstance.destroy(1);
    }

    @Test(expected = InvalidIdException.class)
    public void testInvalidIdUndestroy() throws Exception {
        taskDbInstance.undestroy(1);
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
        TestHelper.createDataFilesWith(testDirectoryString, json);
        taskDbInstance.load();

        // Check that the original task exists
        assertEquals(taskDbInstance.getAll().size(), 1);
        TaskAttributes retrievedTaskAttributes = taskDbInstance.getById(taskAttributes.getId());
        assertEquals(retrievedTaskAttributes.getDescription(), Constants.TEST_TASK_DESCRIPTION_1);
    }

    @Test(expected = InvalidIdException.class)
    public void testRemoveTagFromNonExistentTask() throws Exception {
        taskDbInstance.removeTagById(-1, Constants.TEST_TASK_TAG_1);
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

        // Remove a non-existent tag
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
        assertEquals(taskDbInstance.getByTag(Constants.TEST_TASK_TAG_1).size(), 1);
        assertEquals(taskDbInstance.getByTag(Constants.TEST_TASK_TAG_2).size(), 2);
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

    @Test(expected = InvalidIdException.class)
    public void testAddTagToNull() throws Exception {
        taskDbInstance.addTagById(null, Constants.TEST_TASK_TAG_1);
    }

    @Test(expected = InvalidIdException.class)
    public void testAddTagToInvalidId() throws Exception {
        taskDbInstance.addTagById(1, Constants.TEST_TASK_TAG_1);
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

    @Test(expected = InvalidIdException.class)
    public void testAddReminderToNull() throws Exception {
        taskDbInstance.addReminderById(null, Constants.TEST_TASK_REMINDER_DURATION_1);
    }

    @Test(expected = InvalidIdException.class)
    public void testAddReminderToInvalidId() throws Exception {
        taskDbInstance.addReminderById(1, Constants.TEST_TASK_REMINDER_DURATION_1);
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
