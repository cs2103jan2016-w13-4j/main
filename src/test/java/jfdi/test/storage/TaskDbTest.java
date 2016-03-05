package jfdi.test.storage;

import static org.junit.Assert.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;

import jfdi.storage.Constants;
import jfdi.storage.MainStorage;
import jfdi.storage.data.TaskAttributes;
import jfdi.storage.data.TaskDb;
import jfdi.storage.exceptions.InvalidIdException;
import jfdi.storage.exceptions.NoAttributesChangedException;
import jfdi.storage.serializer.Serializer;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

public class TaskDbTest {

    private static Path testDirectory = null;
    private static String testDirectoryString = null;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        testDirectory = Files.createTempDirectory(Constants.TEST_DIRECTORY_NAME);
        testDirectoryString = testDirectory.toString();
        MainStorage fileStorageInstance = MainStorage.getInstance();
        fileStorageInstance.load(testDirectory.toString());
    }

    @After
    public void tearDown() throws Exception {
        TaskDb.resetProgramStorage();
    }

    @Test
    public void testCreate() throws Exception {
        TaskAttributes taskAttributes = new TaskAttributes();
        taskAttributes.setDescription(Constants.TEST_TASK_DESCRIPTION_1);

        // The database should be empty prior to the task creation
        assertTrue(TaskDb.getAll().isEmpty());

        // Command under test
        TaskDb.createOrUpdate(taskAttributes);

        // Check that the task has been created
        assertEquals(TaskDb.getAll().size(), 1);
        assertEquals(TaskDb.getById(taskAttributes.getId()).getDescription(),
                Constants.TEST_TASK_DESCRIPTION_1);
    }

    @Test
    public void testUpdate() throws Exception {
        TaskAttributes taskAttributes = new TaskAttributes();
        taskAttributes.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
        TaskDb.createOrUpdate(taskAttributes);

        // Check that the task has been created
        assertEquals(TaskDb.getAll().size(), 1);
        assertEquals(TaskDb.getById(1).getDescription(), Constants.TEST_TASK_DESCRIPTION_1);

        // Update the task's description
        taskAttributes.setDescription(Constants.TEST_TASK_DESCRIPTION_2);
        TaskDb.createOrUpdate(taskAttributes);

        // Ensure that object update (and not creation) takes place
        assertEquals(TaskDb.getAll().size(), 1);
        assertEquals(TaskDb.getById(taskAttributes.getId()).getDescription(),
                Constants.TEST_TASK_DESCRIPTION_2);
    }

    @Test(expected = NoAttributesChangedException.class)
    public void testNoChangesUpdate() throws Exception {
        TaskAttributes taskAttributes = new TaskAttributes();
        taskAttributes.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
        TaskDb.createOrUpdate(taskAttributes);

        // The second no-changes update triggers the
        // NoAttributesChangedException
        TaskDb.createOrUpdate(taskAttributes);
    }

    @Test(expected = InvalidIdException.class)
    public void testInvalidIdUpdate() throws Exception {
        TaskAttributes taskAttributes = new TaskAttributes();
        taskAttributes.setDescription(Constants.TEST_TASK_DESCRIPTION_1);

        // The invalid ID will trigger InvalidIdException
        taskAttributes.setId(1);
        TaskDb.createOrUpdate(taskAttributes);
    }

    @Test
    public void testSetAndGetFilePath() {
        Path subdirectory = Paths.get(testDirectory.toString(), Constants.TEST_SUBDIRECTORY_NAME);
        Path expectedTaskPath = Paths.get(subdirectory.toString(), Constants.FILENAME_TASK);

        TaskDb.setFilePath(subdirectory.toString());

        assertEquals(expectedTaskPath, TaskDb.getFilePath());

        // Reset back to the original file path
        TaskDb.setFilePath(testDirectoryString);
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
        ArrayList<TaskAttributes> taskAttributesList = new ArrayList<TaskAttributes>(TaskDb.getAll());
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
        assertEquals(TaskDb.getById(taskAttributes.getId()).getDescription(),
                Constants.TEST_TASK_DESCRIPTION_1);
    }

    @Test
    public void testMarkAsCompleteAndIncomplete() throws Exception {
        // Create a task
        TaskAttributes taskAttributes = new TaskAttributes();
        taskAttributes.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
        taskAttributes.save();

        // Test markAsComplete
        TaskDb.markAsComplete(taskAttributes.getId());
        assertTrue(TaskDb.getById(taskAttributes.getId()).isCompleted());

        // Test markAsIncomplete
        TaskDb.markAsIncomplete(1);
        assertFalse(TaskDb.getById(taskAttributes.getId()).isCompleted());
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
        TaskDb.markAsComplete(taskAttributes.getId());
    }

    @Test(expected = NoAttributesChangedException.class)
    public void testMarkIncompletedAsIncomplete() throws Exception {
        // Create a task
        TaskAttributes taskAttributes = new TaskAttributes();
        taskAttributes.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
        taskAttributes.save();

        // Mark an incomplete task as incomplete
        TaskDb.markAsIncomplete(taskAttributes.getId());
    }

    @Test(expected = InvalidIdException.class)
    public void testMarkInvalidIdAsComplete() throws Exception {
        TaskDb.markAsComplete(1);
    }

    @Test(expected = InvalidIdException.class)
    public void testMarkInvalidIdAsIncomplete() throws Exception {
        TaskDb.markAsIncomplete(1);
    }

    @Test
    public void testDestroyAndUndestroy() throws Exception {
        // Create a task
        TaskAttributes taskAttributes = new TaskAttributes();
        taskAttributes.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
        taskAttributes.save();

        // Check that it has been created
        assertEquals(TaskDb.getAll().size(), 1);

        // Destroy the task
        TaskDb.destroy(taskAttributes.getId());
        assertEquals(TaskDb.getAll().size(), 0);

        // Undestroy the task
        TaskDb.undestroy(taskAttributes.getId());
        assertEquals(TaskDb.getAll().size(), 1);
    }

    @Test(expected = InvalidIdException.class)
    public void testInvalidIdDestroy() throws Exception {
        TaskDb.destroy(1);
    }

    @Test(expected = InvalidIdException.class)
    public void testInvalidIdUndestroy() throws Exception {
        TaskDb.undestroy(1);
    }

    @Test
    public void testLoad() throws Exception {
        // Create a task
        TaskAttributes taskAttributes = new TaskAttributes();
        taskAttributes.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
        taskAttributes.save();

        // Get the JSON form of the current state
        String json = Serializer.serialize(TaskDb.getAll());

        // Remove the task
        TaskDb.destroy(taskAttributes.getId());
        assertTrue(TaskDb.getAll().isEmpty());

        // Create the data file and load from it
        TestHelper.createDataFilesWith(testDirectoryString, json);
        TaskDb.load();

        // Check that the original task exists
        assertEquals(TaskDb.getAll().size(), 1);
        TaskAttributes retrievedTaskAttributes = TaskDb.getById(taskAttributes.getId());
        assertEquals(retrievedTaskAttributes.getDescription(), Constants.TEST_TASK_DESCRIPTION_1);
    }

    @Test(expected = InvalidIdException.class)
    public void testRemoveTagFromNonExistentTask() throws Exception {
        TaskDb.removeTagById(-1, Constants.TEST_TASK_TAG_1);
    }

    @Test(expected = NoAttributesChangedException.class)
    public void testRemoveNonExistentTag() throws Exception {
        // Create a task
        TaskAttributes taskAttributes = new TaskAttributes();
        taskAttributes.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
        taskAttributes.save();

        // Remove a non-existent tag
        TaskDb.removeTagById(taskAttributes.getId(), Constants.TEST_TASK_TAG_1);
    }

    @Test
    public void testRemoveTag() throws Exception {
        // Create a task
        TaskAttributes taskAttributes = new TaskAttributes();
        taskAttributes.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
        taskAttributes.setTags(Constants.TEST_TASK_TAG_1, Constants.TEST_TASK_TAG_2);
        taskAttributes.save();

        // Remove a non-existent tag
        TaskDb.removeTagById(taskAttributes.getId(), Constants.TEST_TASK_TAG_1);

        // Ensure that the correct tag has been removed
        assertFalse(TaskDb.getById(taskAttributes.getId()).hasTag(Constants.TEST_TASK_TAG_1));
        assertTrue(TaskDb.getById(taskAttributes.getId()).hasTag(Constants.TEST_TASK_TAG_2));
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
        assertEquals(TaskDb.getByTag(Constants.TEST_TASK_TAG_1).size(), 1);
        assertEquals(TaskDb.getByTag(Constants.TEST_TASK_TAG_2).size(), 2);
    }

    @Test
    public void testAddTagById() throws Exception {
        // Create task with no tags
        TaskAttributes taskAttributes = new TaskAttributes();
        taskAttributes.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
        taskAttributes.save();

        // Add a tag to it and check that it's persisted
        TaskDb.addTagById(taskAttributes.getId(), Constants.TEST_TASK_TAG_1);
        assertTrue(TaskDb.getById(taskAttributes.getId()).hasTag(Constants.TEST_TASK_TAG_1));
    }

    @Test(expected = InvalidIdException.class)
    public void testAddTagToNull() throws Exception {
        TaskDb.addTagById(null, Constants.TEST_TASK_TAG_1);
    }

    @Test(expected = InvalidIdException.class)
    public void testAddTagToInvalidId() throws Exception {
        TaskDb.addTagById(1, Constants.TEST_TASK_TAG_1);
    }

    @Test(expected = NoAttributesChangedException.class)
    public void testAddDuplicateTag() throws Exception {
        // Create task with tag 1
        TaskAttributes taskAttributes = new TaskAttributes();
        taskAttributes.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
        taskAttributes.setTags(Constants.TEST_TASK_TAG_1);
        taskAttributes.save();

        // Add the same tag to it
        TaskDb.addTagById(taskAttributes.getId(), Constants.TEST_TASK_TAG_1);
    }

    @Test
    public void testAddReminderById() throws Exception {
        // Create task with no reminders
        TaskAttributes taskAttributes = new TaskAttributes();
        taskAttributes.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
        taskAttributes.save();

        // Add a reminder to it and check that it's persisted
        TaskDb.addReminderById(taskAttributes.getId(), Constants.TEST_TASK_REMINDER_DURATION_1);
        ArrayList<Duration> persistedReminderList = new ArrayList<Duration>(TaskDb.getById(taskAttributes.getId()).getReminders());
        assertTrue(persistedReminderList.contains(Constants.TEST_TASK_REMINDER_DURATION_1));
    }

    @Test(expected = InvalidIdException.class)
    public void testAddReminderToNull() throws Exception {
        TaskDb.addReminderById(null, Constants.TEST_TASK_REMINDER_DURATION_1);
    }

    @Test(expected = InvalidIdException.class)
    public void testAddReminderToInvalidId() throws Exception {
        TaskDb.addReminderById(1, Constants.TEST_TASK_REMINDER_DURATION_1);
    }

    @Test(expected = NoAttributesChangedException.class)
    public void testAddDuplicateReminder() throws Exception {
        // Create task with reminder 1
        TaskAttributes taskAttributes = new TaskAttributes();
        taskAttributes.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
        taskAttributes.setReminders(Constants.TEST_TASK_REMINDER_DURATION_1);
        taskAttributes.save();

        // Add the same reminder to it
        TaskDb.addReminderById(taskAttributes.getId(), Constants.TEST_TASK_REMINDER_DURATION_1);
    }

}
