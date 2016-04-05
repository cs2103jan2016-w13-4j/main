//@@author A0121621Y

package jfdi.test.storage;

import static org.junit.Assert.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;

import jfdi.storage.Constants;
import jfdi.storage.apis.MainStorage;
import jfdi.storage.apis.TaskAttributes;
import jfdi.storage.apis.TaskDb;
import jfdi.storage.entities.Task;
import jfdi.storage.exceptions.InvalidIdException;
import jfdi.storage.exceptions.DuplicateTaskException;
import jfdi.storage.exceptions.InvalidTaskParametersException;
import jfdi.storage.exceptions.NoAttributesChangedException;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.ocpsoft.prettytime.shade.edu.emory.mathcs.backport.java.util.Collections;

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

    @Test
    public void testIsValid() throws Exception {
        // An invalid TaskAttributes (e.g. no description) should return false
        TaskAttributes taskAttributes = new TaskAttributes();
        assertFalse(taskAttributes.isValid());

        // An invalid TaskAttributes (e.g. blank description) should return false
        taskAttributes.setDescription(" ");
        assertFalse(taskAttributes.isValid());

        // A valid TaskAttributes should return true
        taskAttributes.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
        assertTrue(taskAttributes.isValid());
    }

    @Test(expected = InvalidTaskParametersException.class)
    public void testInvalidParametersSave() throws Exception {
        // Save an invalid TaskAttributes object (without parameters)
        // This should throw an exception
        new TaskAttributes().save();
    }

    @Test(expected = InvalidTaskParametersException.class)
    public void testStartAfterEndDateTime() throws Exception {
        // Save a task with start date after end date
        TaskAttributes taskAttributes = new TaskAttributes();
        taskAttributes.setStartDateTime(Constants.TEST_TASK_ENDDATETIME);
        taskAttributes.setEndDateTime(Constants.TEST_TASK_STARTDATETIME);
        taskAttributes.save();
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

    @Test(expected = DuplicateTaskException.class)
    public void testSaveDuplicateTasks() throws Exception {
        // Save the first Task
        TaskAttributes taskAttributes = new TaskAttributes();
        taskAttributes.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
        taskAttributes.setStartDateTime(Constants.TEST_TASK_STARTDATETIME);
        taskAttributes.setEndDateTime(Constants.TEST_TASK_ENDDATETIME);
        taskAttributes.save();

        // Save another identical Task
        // This should trigger the exception
        taskAttributes.setId(null);
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

    @Test
    public void testIsOverdueAndIsUpcoming() throws Exception {
        // Task 1 has start date-time which is overdue
        TaskAttributes task1 = new TaskAttributes();
        task1.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
        task1.setStartDateTime(LocalDateTime.now().minusDays(1));
        assertTrue(task1.isOverdue());
        assertFalse(task1.isUpcoming());

        // A completed task should not be upcoming/overdue
        task1.setCompleted(true);
        assertFalse(task1.isOverdue());
        assertFalse(task1.isUpcoming());

        // Task 2 has start date-time which is upcoming
        TaskAttributes task2 = new TaskAttributes();
        task2.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
        task2.setStartDateTime(LocalDateTime.now().plusDays(1));
        assertFalse(task2.isOverdue());
        assertTrue(task2.isUpcoming());

        // A completed task should not be upcoming/overdue
        task2.setCompleted(true);
        assertFalse(task2.isOverdue());
        assertFalse(task2.isUpcoming());

        // Task 3 has end date-time which is overdue
        TaskAttributes task3 = new TaskAttributes();
        task3.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
        task3.setEndDateTime(LocalDateTime.now().minusDays(1));
        assertTrue(task3.isOverdue());
        assertFalse(task3.isUpcoming());

        // A completed task should not be upcoming/overdue
        task3.setCompleted(true);
        assertFalse(task3.isOverdue());
        assertFalse(task3.isUpcoming());

        // Task 4 has end date-time which is upcoming
        TaskAttributes task4 = new TaskAttributes();
        task4.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
        task4.setEndDateTime(LocalDateTime.now().plusDays(1));
        assertFalse(task4.isOverdue());
        assertTrue(task4.isUpcoming());

        // A completed task should not be upcoming/overdue
        task4.setCompleted(true);
        assertFalse(task4.isOverdue());
        assertFalse(task4.isUpcoming());

        // Task 5 is an event which started 1 day ago (overdue)
        TaskAttributes task5 = new TaskAttributes();
        task5.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
        task5.setStartDateTime(LocalDateTime.now().minusDays(1));
        task5.setEndDateTime(LocalDateTime.now().plusDays(1));
        assertTrue(task5.isOverdue());
        assertFalse(task5.isUpcoming());

        // A completed task should not be upcoming/overdue
        task5.setCompleted(true);
        assertFalse(task5.isOverdue());
        assertFalse(task5.isUpcoming());

        // Task 6 is an event which will start 1 day later (upcoming)
        TaskAttributes task6 = new TaskAttributes();
        task6.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
        task6.setStartDateTime(LocalDateTime.now().plusDays(1));
        task6.setEndDateTime(LocalDateTime.now().plusDays(2));
        assertFalse(task6.isOverdue());
        assertTrue(task6.isUpcoming());

        // A completed task should not be upcoming/overdue
        task6.setCompleted(true);
        assertFalse(task6.isOverdue());
        assertFalse(task6.isUpcoming());

        // A floating task should not be upcoming/overdue
        TaskAttributes task7 = new TaskAttributes();
        task7.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
        assertFalse(task7.isOverdue());
        assertFalse(task7.isUpcoming());
    }

    @Test
    public void testSorting() {
        // Add the earliest event
        TaskAttributes task1 = new TaskAttributes();
        task1.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
        task1.setStartDateTime(LocalDateTime.now().minusDays(7));
        task1.setEndDateTime(LocalDateTime.now().plusDays(7));

        // Add the second earliest point task
        TaskAttributes task2 = new TaskAttributes();
        task2.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
        task2.setStartDateTime(LocalDateTime.now().minusDays(6));

        // Add the latest deadline task
        TaskAttributes task3 = new TaskAttributes();
        task3.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
        task3.setEndDateTime(LocalDateTime.now().minusDays(4));

        // Add the second latest event
        TaskAttributes task4 = new TaskAttributes();
        task4.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
        task4.setStartDateTime(LocalDateTime.now().minusDays(5));
        task4.setEndDateTime(LocalDateTime.now().minusDays(4));

        // Add the TaskAttributes to an ArrayList
        ArrayList<TaskAttributes> taskAttributes = new ArrayList<TaskAttributes>();
        taskAttributes.add(task4);
        taskAttributes.add(task3);
        taskAttributes.add(task2);
        taskAttributes.add(task1);

        // Command under test
        Collections.sort(taskAttributes);

        // Check that the TaskAttributes are correctly sorted
        assertEquals(0, taskAttributes.indexOf(task1));
        assertEquals(1, taskAttributes.indexOf(task2));
        assertEquals(2, taskAttributes.indexOf(task4));
        assertEquals(3, taskAttributes.indexOf(task3));
    }

    @Test
    public void testHashingAndEquals() throws Exception {
        // Create the first task (an event)
        TaskAttributes task1 = new TaskAttributes();
        task1.setId(1);
        task1.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
        task1.setStartDateTime(LocalDateTime.now().minusDays(7));
        task1.setEndDateTime(LocalDateTime.now().plusDays(7));

        // Create a duplicate of task 1
        TaskAttributes task1Duplicate = new TaskAttributes();
        task1Duplicate.setId(1);
        task1Duplicate.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
        task1Duplicate.setStartDateTime(LocalDateTime.now().minusDays(7));
        task1Duplicate.setEndDateTime(LocalDateTime.now().plusDays(7));

        // Assert that the task and its duplicate are equal and has the same hashCode
        assertEquals(task1, task1Duplicate);
        assertEquals(task1.hashCode(), task1Duplicate.hashCode());

        // Create the second task (a point task)
        TaskAttributes task2 = new TaskAttributes();
        task2.setId(2);
        task2.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
        task2.setStartDateTime(LocalDateTime.now().minusDays(6));

        // Create a duplicate of the second task
        TaskAttributes task2Duplicate = new TaskAttributes();
        task2Duplicate.setId(2);
        task2Duplicate.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
        task2Duplicate.setStartDateTime(LocalDateTime.now().minusDays(6));

        // Assert that the task and its duplicate are equal and has the same hashCode
        assertEquals(task2, task2Duplicate);
        assertEquals(task2.hashCode(), task2Duplicate.hashCode());

        // Create the third task (a deadline task)
        TaskAttributes task3 = new TaskAttributes();
        task3.setId(3);
        task3.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
        task3.setEndDateTime(LocalDateTime.now().minusDays(4));

        // Create a duplicate of the third task
        TaskAttributes task3Duplicate = new TaskAttributes();
        task3Duplicate.setId(3);
        task3Duplicate.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
        task3Duplicate.setEndDateTime(LocalDateTime.now().minusDays(4));

        // Assert that the task and its duplicate are equal and has the same hashCode
        assertEquals(task3, task3Duplicate);
        assertEquals(task3.hashCode(), task3Duplicate.hashCode());

        // Add the taskAttributes to a HashSet
        TaskAttributes[] taskAttributes = {task1, task1Duplicate, task2, task2Duplicate, task3, task3Duplicate};
        HashSet<TaskAttributes> taskAttributesSet = new HashSet<TaskAttributes>();
        for (TaskAttributes task : taskAttributes) {
            taskAttributesSet.add(task);
        }

        // The size of the HashSet should be the number of unique tasks
        assertEquals(3, taskAttributesSet.size());

        // Equals should return false if the compared object is of different type
        assertFalse(task1.equals(null));
        assertFalse(task1.equals(new String()));
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
