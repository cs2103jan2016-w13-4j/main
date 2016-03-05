package jfdi.test.storage;

import static org.junit.Assert.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.HashSet;
import java.util.TreeSet;

import jfdi.storage.Constants;
import jfdi.storage.MainStorage;
import jfdi.storage.data.Task;
import jfdi.storage.data.TaskAttributes;
import jfdi.storage.data.TaskDb;
import jfdi.storage.exceptions.InvalidIdException;
import jfdi.storage.exceptions.InvalidTaskParametersException;
import jfdi.storage.exceptions.NoAttributesChangedException;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

public class TaskAttributesTest {

    private static Path testDirectory = null;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        testDirectory = Files.createTempDirectory(Constants.TEST_DIRECTORY_NAME);
        MainStorage fileStorageInstance = MainStorage.getInstance();
        fileStorageInstance.load(testDirectory.toString());
    }

    @After
    public void tearDown() throws Exception {
        TaskDb.resetProgramStorage();
    }

    @Test
    public void testSettersAndGetters() {
        // Use the setters to set the task's attributes
        TaskAttributes taskAttributes = new TaskAttributes();
        taskAttributes.setId(1);
        taskAttributes.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
        taskAttributes.setStartDateTime(Constants.TEST_TASK_STARTDATETIME);
        taskAttributes.setEndDateTime(Constants.TEST_TASK_ENDDATETIME);
        taskAttributes.setTags(Constants.TEST_TASK_TAG_1);
        taskAttributes.setReminders(Constants.TEST_TASK_REMINDER_DURATION_1);
        taskAttributes.setCompleted(true);

        // Assert that the getter returns the same attributes
        assertEquals(taskAttributes.getId(), new Integer(1));
        assertEquals(taskAttributes.getDescription(), Constants.TEST_TASK_DESCRIPTION_1);
        assertEquals(taskAttributes.getStartDateTime(), Constants.TEST_TASK_STARTDATETIME);
        assertEquals(taskAttributes.getEndDateTime(), Constants.TEST_TASK_ENDDATETIME);
        HashSet<String> tags = taskAttributes.getTags();
        assertEquals(tags.size(), 1);
        assertTrue(tags.contains(Constants.TEST_TASK_TAG_1));
        TreeSet<Duration> reminders = taskAttributes.getReminders();
        assertEquals(reminders.size(), 1);
        assertTrue(reminders.contains(Constants.TEST_TASK_REMINDER_DURATION_1));
        assertEquals(taskAttributes.isCompleted(), true);
    }

    @Test
    public void testConstructor() {
        // Generate a TaskAttribute from a Task
        Task task = getSimpleTask();
        TaskAttributes taskAttributes = new TaskAttributes(task);

        // Check for equivalence in all attributes
        assertEquals(task.getId(), taskAttributes.getId());
        assertEquals(task.getDescription(), taskAttributes.getDescription());
        assertEquals(task.getStartDateTime(), taskAttributes.getStartDateTime());
        assertEquals(task.getEndDateTime(), taskAttributes.getEndDateTime());
        assertEquals(task.getTags(), taskAttributes.getTags());
        assertEquals(task.getReminders(), taskAttributes.getReminders());
    }

    @Test(expected = InvalidTaskParametersException.class)
    public void testInvalidParametersSave() throws Exception {
        assertTrue(TaskDb.getAll().isEmpty());
        TaskAttributes taskAttributes = new TaskAttributes();

        // Omit all attributes and save
        taskAttributes.save();
    }

    @Test(expected = NoAttributesChangedException.class)
    public void testNoChangesSave() throws Exception {
        assertTrue(TaskDb.getAll().isEmpty());
        TaskAttributes taskAttributes = new TaskAttributes();
        taskAttributes.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
        taskAttributes.save();

        // The second save with no changes
        taskAttributes.save();
    }

    @Test(expected = InvalidIdException.class)
    public void testInvalidIdSave() throws Exception {
        assertTrue(TaskDb.getAll().isEmpty());
        TaskAttributes taskAttributes = new TaskAttributes();
        taskAttributes.setDescription(Constants.TEST_TASK_DESCRIPTION_1);

        // Point to an invalid ID and save
        taskAttributes.setId(1);
        taskAttributes.save();
    }

    @Test
    public void testCreateOnSave() {
        try {
            // Make sure that the database starts off empty
            assertTrue(TaskDb.getAll().isEmpty());
            TaskAttributes taskAttributes = new TaskAttributes();
            taskAttributes.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
            taskAttributes.setStartDateTime(Constants.TEST_TASK_STARTDATETIME);
            taskAttributes.setEndDateTime(Constants.TEST_TASK_ENDDATETIME);
            taskAttributes.setTags(Constants.TEST_TASK_TAG_1);
            taskAttributes.setReminders(Constants.TEST_TASK_REMINDER_DURATION_1);
            taskAttributes.save();

            // Verify that a task has been created
            assertEquals(TaskDb.getAll().size(), 1);

            // Check that the task has been created with the correct attributes
            TaskAttributes taskAttributes2 = TaskDb.getById(1);
            assertEquals(taskAttributes2.getId(), new Integer(1));
            assertEquals(taskAttributes2.getDescription(), Constants.TEST_TASK_DESCRIPTION_1);
            assertEquals(taskAttributes2.getStartDateTime(), Constants.TEST_TASK_STARTDATETIME);
            assertEquals(taskAttributes2.getEndDateTime(), Constants.TEST_TASK_ENDDATETIME);
            assertTrue(taskAttributes2.hasTag(Constants.TEST_TASK_TAG_1));
            TreeSet<Duration> reminders = taskAttributes2.getReminders();
            assertEquals(reminders.size(), 1);
            assertTrue(reminders.contains(Constants.TEST_TASK_REMINDER_DURATION_1));
            assertEquals(taskAttributes2.isCompleted(), false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpdateOnSave() {
        try {
            // Make sure that the database starts off empty
            assertTrue(TaskDb.getAll().isEmpty());
            TaskAttributes taskAttributes = new TaskAttributes();
            taskAttributes.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
            taskAttributes.save();

            // Verify that a task has been created
            assertEquals(TaskDb.getAll().size(), 1);

            // Add more attributes to it
            taskAttributes.setStartDateTime(Constants.TEST_TASK_STARTDATETIME);
            taskAttributes.setEndDateTime(Constants.TEST_TASK_ENDDATETIME);
            taskAttributes.setTags(Constants.TEST_TASK_TAG_1);
            taskAttributes.setReminders(Constants.TEST_TASK_REMINDER_DURATION_1);
            taskAttributes.setCompleted(true);
            taskAttributes.save();

            // Verify that the attributes have been updated
            TaskAttributes taskAttributes2 = TaskDb.getById(1);
            assertEquals(taskAttributes2.getId(), new Integer(1));
            assertEquals(taskAttributes2.getDescription(), Constants.TEST_TASK_DESCRIPTION_1);
            assertEquals(taskAttributes2.getStartDateTime(), Constants.TEST_TASK_STARTDATETIME);
            assertEquals(taskAttributes2.getEndDateTime(), Constants.TEST_TASK_ENDDATETIME);
            assertTrue(taskAttributes2.hasTag(Constants.TEST_TASK_TAG_1));
            TreeSet<Duration> reminders = taskAttributes2.getReminders();
            assertEquals(reminders.size(), 1);
            assertTrue(reminders.contains(Constants.TEST_TASK_REMINDER_DURATION_1));
            assertEquals(taskAttributes2.isCompleted(), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testToEntity() {
        // Generate a TaskAttribute and get its entity
        Task task = getSimpleTask();
        TaskAttributes taskAttributes = new TaskAttributes(task);
        Task taskMirror = taskAttributes.toEntity();

        // Check for equivalence in all attributes
        assertEquals(taskAttributes.getId(), taskMirror.getId());
        assertEquals(taskAttributes.getDescription(), taskMirror.getDescription());
        assertEquals(taskAttributes.getStartDateTime(), taskMirror.getStartDateTime());
        assertEquals(taskAttributes.getEndDateTime(), taskMirror.getEndDateTime());
        assertEquals(taskAttributes.getTags(), taskMirror.getTags());
        assertEquals(taskAttributes.getReminders(), taskMirror.getReminders());
    }

    @Test
    public void testAddTag() {
        // Generate the new TaskAttributes
        TaskAttributes taskAttributes = new TaskAttributes();
        assertTrue(taskAttributes.getTags().isEmpty());

        // Add a new tag
        taskAttributes.addTag(Constants.TEST_TASK_TAG_1);
        assertEquals(taskAttributes.getTags().size(), 1);
        assertTrue(taskAttributes.getTags().contains(Constants.TEST_TASK_TAG_1));

        // Add the same tag - it should not be doubly-added
        assertFalse(taskAttributes.addTag(Constants.TEST_TASK_TAG_1));
        assertEquals(taskAttributes.getTags().size(), 1);

        // Add a second unique tag
        taskAttributes.addTag(Constants.TEST_TASK_TAG_2);
        assertEquals(taskAttributes.getTags().size(), 2);
        assertTrue(taskAttributes.getTags().contains(Constants.TEST_TASK_TAG_2));
    }

    @Test
    public void testRemoveTag() {
        // Generate the new TaskAttributes
        TaskAttributes taskAttributes = new TaskAttributes();
        taskAttributes.addTag(Constants.TEST_TASK_TAG_1);
        taskAttributes.addTag(Constants.TEST_TASK_TAG_2);
        assertEquals(taskAttributes.getTags().size(), 2);

        // Try removing a non-existent tag - it shouldn't work
        assertFalse(taskAttributes.removeTag(Constants.TEST_TASK_DESCRIPTION_1));
        assertEquals(taskAttributes.getTags().size(), 2);

        // Remove a valid tag
        assertTrue(taskAttributes.removeTag(Constants.TEST_TASK_TAG_1));
        assertEquals(taskAttributes.getTags().size(), 1);

        // Remove the same tag - it shouldn't work
        assertFalse(taskAttributes.removeTag(Constants.TEST_TASK_TAG_1));
        assertEquals(taskAttributes.getTags().size(), 1);

        // Remove the last tag
        assertTrue(taskAttributes.removeTag(Constants.TEST_TASK_TAG_2));
        assertEquals(taskAttributes.getTags().size(), 0);
    }

    @Test
    public void testAddReminder() {
        // Generate the new TaskAttributes
        TaskAttributes taskAttributes = new TaskAttributes();
        assertTrue(taskAttributes.getReminders().isEmpty());

        // Add a new reminder
        taskAttributes.addReminder(Constants.TEST_TASK_REMINDER_DURATION_1);
        assertEquals(taskAttributes.getReminders().size(), 1);
        assertTrue(taskAttributes.getReminders().contains(Constants.TEST_TASK_REMINDER_DURATION_1));

        // Add the same reminder - it should not be doubly-added
        assertFalse(taskAttributes.addReminder(Constants.TEST_TASK_REMINDER_DURATION_1));
        assertEquals(taskAttributes.getReminders().size(), 1);

        // Add a second unique reminder
        taskAttributes.addReminder(Constants.TEST_TASK_REMINDER_DURATION_2);
        assertEquals(taskAttributes.getReminders().size(), 2);
        assertTrue(taskAttributes.getReminders().contains(Constants.TEST_TASK_REMINDER_DURATION_2));
    }

    @Test
    public void testEqualTo() {
        // Generate a task
        Task task = getSimpleTask();

        // Set identical attributes
        TaskAttributes taskAttributes = new TaskAttributes();
        taskAttributes.setId(1);
        taskAttributes.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
        taskAttributes.setStartDateTime(Constants.TEST_TASK_STARTDATETIME);
        taskAttributes.setEndDateTime(Constants.TEST_TASK_ENDDATETIME);
        taskAttributes.addTag(Constants.TEST_TASK_TAG_1);
        taskAttributes.addReminder(Constants.TEST_TASK_REMINDER_DURATION_1);

        // Make sure they are equivalent
        assertTrue(taskAttributes.equalTo(task));
    }

    /**
     * @return a new Task with all of the first constants as its attributes
     */
    private Task getSimpleTask() {
        // Prepare the tags and reminders
        HashSet<String> tags = new HashSet<String>();
        tags.add(Constants.TEST_TASK_TAG_1);
        TreeSet<Duration> reminders = new TreeSet<Duration>();
        reminders.add(Constants.TEST_TASK_REMINDER_DURATION_1);

        // Generate the new Task
        Task task = new Task(
                1,
                Constants.TEST_TASK_DESCRIPTION_1,
                Constants.TEST_TASK_STARTDATETIME,
                Constants.TEST_TASK_ENDDATETIME,
                tags,
                reminders
                );
        return task;
    }

}
