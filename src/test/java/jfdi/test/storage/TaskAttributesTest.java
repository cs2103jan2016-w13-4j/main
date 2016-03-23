package jfdi.test.storage;

import static org.junit.Assert.*;

import java.nio.file.Files;
import java.nio.file.Path;

import jfdi.storage.Constants;
import jfdi.storage.apis.MainStorage;
import jfdi.storage.apis.TaskAttributes;
import jfdi.storage.apis.TaskDb;
import jfdi.storage.entities.Task;
import jfdi.storage.exceptions.InvalidIdException;
import jfdi.storage.exceptions.InvalidTaskParametersException;
import jfdi.storage.exceptions.NoAttributesChangedException;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

public class TaskAttributesTest {

    private static Path testDirectory = null;
    private static TaskDb taskDbInstance = null;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        testDirectory = Files.createTempDirectory(Constants.TEST_DIRECTORY_NAME);
        MainStorage.getInstance().load(testDirectory.toString());
        taskDbInstance = TaskDb.getInstance();
    }

    @After
    public void tearDown() throws Exception {
        taskDbInstance.reset();
    }

    @Test
    public void testSettersAndGetters() {
        // Use the setters to set the task's attributes
        TaskAttributes taskAttributes = new TaskAttributes();
        taskAttributes.setId(1);
        taskAttributes.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
        taskAttributes.setStartDateTime(Constants.TEST_TASK_STARTDATETIME);
        taskAttributes.setEndDateTime(Constants.TEST_TASK_ENDDATETIME);
        taskAttributes.setCompleted(true);

        // Assert that the getter returns the same attributes
        assertEquals(new Integer(1), taskAttributes.getId());
        assertEquals(Constants.TEST_TASK_DESCRIPTION_1, taskAttributes.getDescription());
        assertEquals(Constants.TEST_TASK_STARTDATETIME, taskAttributes.getStartDateTime());
        assertEquals(Constants.TEST_TASK_ENDDATETIME, taskAttributes.getEndDateTime());
        assertEquals(true, taskAttributes.isCompleted());
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
    }

    @Test(expected = InvalidTaskParametersException.class)
    public void testInvalidParametersSave() throws Exception {
        // Save an invalid TaskAttributes object (without parameters)
        // This should throw an exception
        new TaskAttributes().save();
    }

    @Test(expected = NoAttributesChangedException.class)
    public void testNoChangesSave() throws Exception {
        // Persist the Task to storage
        TaskAttributes taskAttributes = new TaskAttributes();
        taskAttributes.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
        taskAttributes.save();

        // Call a second save with no changes
        // This should throw an exception
        taskAttributes.save();
    }

    @Test(expected = InvalidIdException.class)
    public void testInvalidIdSave() throws Exception {
        // Ensure that there are no other tasks in the DB first
        assertTrue(taskDbInstance.getAll().isEmpty());

        // Create a Task with an invalid ID and save
        TaskAttributes taskAttributes = new TaskAttributes();
        taskAttributes.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
        taskAttributes.setId(1);

        // This should trigger the exception
        taskAttributes.save();
    }

    @Test
    public void testCreateOnSave() throws Exception {
        // Make sure that the database starts off empty
        assertTrue(taskDbInstance.getAll().isEmpty());

        // We create a Task
        TaskAttributes taskAttributes = new TaskAttributes();
        taskAttributes.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
        taskAttributes.setStartDateTime(Constants.TEST_TASK_STARTDATETIME);
        taskAttributes.setEndDateTime(Constants.TEST_TASK_ENDDATETIME);
        taskAttributes.save();

        // Verify that a task has been created
        assertEquals(1, taskDbInstance.getAll().size());

        // Check that the task has been created with the correct attributes
        TaskAttributes taskAttributes2 = taskDbInstance.getById(1);
        assertEquals(new Integer(1), taskAttributes2.getId());
        assertEquals(Constants.TEST_TASK_DESCRIPTION_1, taskAttributes2.getDescription());
        assertEquals(Constants.TEST_TASK_STARTDATETIME, taskAttributes2.getStartDateTime());
        assertEquals(Constants.TEST_TASK_ENDDATETIME, taskAttributes2.getEndDateTime());
        assertEquals(false, taskAttributes2.isCompleted());
    }

    @Test
    public void testUpdateOnSave() throws Exception {
        // Make sure that the database starts off empty
        assertTrue(taskDbInstance.getAll().isEmpty());

        // Create a Task
        TaskAttributes taskAttributes = new TaskAttributes();
        taskAttributes.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
        taskAttributes.save();

        // Verify that a task has been created
        assertEquals(1, taskDbInstance.getAll().size());

        // Add more attributes to it
        taskAttributes.setStartDateTime(Constants.TEST_TASK_STARTDATETIME);
        taskAttributes.setEndDateTime(Constants.TEST_TASK_ENDDATETIME);
        taskAttributes.setCompleted(true);
        taskAttributes.save();

        // Verify that the attributes have been updated
        TaskAttributes taskAttributes2 = taskDbInstance.getById(1);
        assertEquals(new Integer(1), taskAttributes2.getId());
        assertEquals(Constants.TEST_TASK_DESCRIPTION_1, taskAttributes2.getDescription());
        assertEquals(Constants.TEST_TASK_STARTDATETIME, taskAttributes2.getStartDateTime());
        assertEquals(Constants.TEST_TASK_ENDDATETIME, taskAttributes2.getEndDateTime());
        assertEquals(true, taskAttributes2.isCompleted());
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

        // Make sure they are equivalent
        assertTrue(taskAttributes.equalTo(task));
    }

    /**
     * @return a new Task with all of the first constants as its attributes
     */
    private Task getSimpleTask() {
        // Generate the new Task
        Task task = new Task(
                1,
                Constants.TEST_TASK_DESCRIPTION_1,
                Constants.TEST_TASK_STARTDATETIME,
                Constants.TEST_TASK_ENDDATETIME
                );
        return task;
    }

}
